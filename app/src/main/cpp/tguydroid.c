#include <jni.h>
#include <libtguy.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include <stddef.h>

typedef struct {
    TrashGuyState *tg;
    char *res;
    char text[];
} nat_ctx;

JNIEXPORT jlong JNICALL
Java_com_wirtos_tguydroid_TGuy_tguy_1jni_1ctor(JNIEnv *env, jobject thiz,
                                               jbyteArray text, jint spacing) {
    (void) thiz;
    const size_t u8len = (*env)->GetArrayLength(env, text);
    nat_ctx *ctx = NULL;
    TrashGuyState *tg = NULL;
    char *res = NULL;

    ctx = malloc(offsetof(nat_ctx, text) + u8len);
    if (ctx == NULL) goto fail;

    (*env)->GetByteArrayRegion(env, text, 0, u8len, (int8_t *) ctx->text);

    tg = tguy_from_utf8(ctx->text, u8len, spacing);
    if (tg == NULL) goto fail;

    res = malloc(tguy_get_bsize(tg));
    if (res == NULL) goto fail;

    ctx->tg = tg;
    ctx->res = res;
    return (jlong) ctx;

    fail:
    free(ctx);
    free(tg);
    free(res);
    return (jlong) NULL;

}

JNIEXPORT void JNICALL
Java_com_wirtos_tguydroid_TGuy_tguy_1jni_1dtor(JNIEnv *env, jobject thiz, jlong tgobj) {
    (void) env, (void) thiz;
    nat_ctx *ctx = (nat_ctx *) tgobj;
    if (ctx) {
        tguy_free(ctx->tg);
        free(ctx->res);
        free(ctx);
    }
}

JNIEXPORT void JNICALL
Java_com_wirtos_tguydroid_TGuy_tguy_1jni_1set_1frame(JNIEnv *env, jobject thiz, jlong tgobj,
                                                     jint frame) {
    (void) env, (void) thiz;
    nat_ctx *ctx = (nat_ctx *) tgobj;
    tguy_set_frame(ctx->tg, frame);
}

JNIEXPORT jint JNICALL
Java_com_wirtos_tguydroid_TGuy_tguy_1jni_1get_1frames_1count(JNIEnv *env, jobject thiz,
                                                             jlong tgobj) {
    (void) env, (void) thiz;
    nat_ctx *ctx = (nat_ctx *) tgobj;
    return tguy_get_frames_count(ctx->tg);
}

JNIEXPORT jbyteArray JNICALL
Java_com_wirtos_tguydroid_TGuy_tguy_1jni_1get_1text(JNIEnv *env, jobject thiz,
                                                    jlong tgobj) {
    (void) thiz;
    nat_ctx *ctx = (nat_ctx *) tgobj;
    size_t len = tguy_sprint(ctx->tg, ctx->res);
    jbyteArray ret = (*env)->NewByteArray(env, len);
    if (ret == NULL) {
        return NULL;
    }

    (*env)->SetByteArrayRegion(env, ret, 0, len, (int8_t *) ctx->res);
    return ret;
}
