int main(int arg) {
    Q list = (3 . (4 . (((56 . (5 . nil)) . nil) . (26 . (2 . ((8 . nil) . nil))))));
    int result = isList(list);
    return result;
}

int isList(Q q) {
    if (isNil(q) != 0)
        return 1;
    if (isAtom(q) != 0)
        return 0;

    return isList(right((Ref)q));
}
