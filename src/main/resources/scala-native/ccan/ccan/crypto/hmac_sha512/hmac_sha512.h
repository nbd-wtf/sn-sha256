#ifndef CCAN_CRYPTO_HMAC_SHA512_H
#define CCAN_CRYPTO_HMAC_SHA512_H
/* BSD-MIT - see LICENSE file for details */
#include "../../../config.h"
#include <stdint.h>
#include <stdlib.h>
#include "../sha512/sha512.h"

/* Number of bytes per block. */
#define HMAC_SHA512_BLOCKSIZE 128

/**
 * struct hmac_sha512 - structure representing a completed HMAC.
 */
struct hmac_sha512 {
	struct sha512 sha;
};

/**
 * hmac_sha512 - return hmac of an object with a key.
 * @hmac: the hmac to fill in
 * @k: pointer to the key,
 * @ksize: the number of bytes pointed to by @k
 * @d: pointer to memory,
 * @dsize: the number of bytes pointed to by @d
 */
void hmac_sha512(struct hmac_sha512 *hmac,
		 const void *k, size_t ksize,
		 const void *d, size_t dsize);

/**
 * struct hmac_sha512_ctx - structure to store running context for hmac_sha512
 */
struct hmac_sha512_ctx {
	struct sha512_ctx sha;
	uint64_t k_opad[HMAC_SHA512_BLOCKSIZE / sizeof(uint64_t)];
};

/**
 * hmac_sha512_init - initialize an HMAC_SHA512 context.
 * @ctx: the hmac_sha512_ctx to initialize
 * @k: pointer to the key,
 * @ksize: the number of bytes pointed to by @k
 *
 * This must be called before hmac_sha512_update or hmac_sha512_done.
 *
 * If it was already initialized, this forgets anything which was
 * hashed before.
 *
 * Example:
 * static void hmac_all(const char *key,
 *			const char **arr, struct hmac_sha512 *hash)
 * {
 *	size_t i;
 *	struct hmac_sha512_ctx ctx;
 *
 *	hmac_sha512_init(&ctx, key, strlen(key));
 *	for (i = 0; arr[i]; i++)
 *		hmac_sha512_update(&ctx, arr[i], strlen(arr[i]));
 *	hmac_sha512_done(&ctx, hash);
 * }
 */
void hmac_sha512_init(struct hmac_sha512_ctx *ctx,
		      const void *k, size_t ksize);

/**
 * hmac_sha512_update - include some memory in the hash.
 * @ctx: the hmac_sha512_ctx to use
 * @p: pointer to memory,
 * @size: the number of bytes pointed to by @p
 *
 * You can call this multiple times to hash more data, before calling
 * hmac_sha512_done().
 */
void hmac_sha512_update(struct hmac_sha512_ctx *ctx, const void *p, size_t size);

/**
 * hmac_sha512_done - finish HMAC_SHA512 and return the hash
 * @ctx: the hmac_sha512_ctx to complete
 * @res: the hash to return.
 *
 * Note that @ctx is *destroyed* by this, and must be reinitialized.
 * To avoid that, pass a copy instead.
 */
void hmac_sha512_done(struct hmac_sha512_ctx *hmac_sha512, struct hmac_sha512 *res);
#endif /* CCAN_CRYPTO_HMAC_SHA512_H */
