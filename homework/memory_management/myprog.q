mutable Q main(int n) {
    if (n == 1) return q1();
    else if (n == 2) return q2();
    else if (n == 3) return q3();
    else if (n == 4) return q4();

    return 0;
}

mutable Q q1() {
    mutable Ref list = nil;
    mutable int i = 0;

    while (i < 17) {
        list = i . list;
        i = i + 1;
    }

    return list;
}

mutable Q q2() {
    mutable Ref cycle = nil;
    cycle = 0 . cycle;
    setRight(cycle, cycle);
    cycle = nil;

    mutable Ref list = nil;
    mutable int i = 0;

    while (i < 16) {
        list = i . list;
        i = i + 1;
    }

    return list;
}

mutable Q q3() {
    mutable Ref list = nil;
    mutable int i = 0;

    while (i < 17) {
        list = i . list;
        free(list);
        i = i + 1;
    }

    return 1;
}

mutable Q q4() {
    mutable Ref list = nil;
    mutable int i = 0;

    while (i < 16) {
        list = i . list;
        i = i + 1;
    }

    list = nil;
    list = 0 . list;

    return list;
}
