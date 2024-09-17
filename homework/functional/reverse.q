Ref main(int arg) {
    Ref list = (3 . (4 . (((56 . (5 . nil)) . nil) . (26 . (2 . ((8 . nil) . nil))))));
    Ref result = reverse(list);
    return result;
}

Ref append(Ref list, Q q) {
    if (isNil(list) != 0)
        return q . nil;
    return left(list) . append((Ref)right(list), q);
}

Ref reverse(Ref q) {
    if (isNil(q) != 0)
        return q;

    return append(reverse((Ref)right(q)), left(q));
}
