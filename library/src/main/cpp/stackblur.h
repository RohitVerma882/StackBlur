#ifndef STACKBLUR_H
#define STACKBLUR_H

#ifndef MAX
#define MAX(x, y)((x) > (y)) ? (x) : (y)
#endif
#ifndef MIN
#define MIN(x, y)((x) < (y)) ? (x) : (y)
#endif
#ifndef SQUARE
#define SQUARE(i)((i) * (i))
#endif

inline static void zeroClearInt(int * p, size_t count) {
    memset(p, 0, sizeof(int) * count);
}

#endif