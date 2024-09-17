/* Question 1 */
int isList(Ref q) {
    if (isNil(q) != 0)
        return 1;
    if (isAtom(q) != 0)
        return 0;

    return isList((Ref)right(q));
}

/* Question 2 */
Ref append(Ref list1, Ref list2) {
    if (isNil(list1) != 0)
        return list2;
    return left(list1) . append((Ref)right(list1), list2);
}

/* Question 3 */
Ref appendQ(Ref list, Q q) {
    if (isNil(list) != 0)
        return q . nil;
    return left(list) . appendQ((Ref)right(list), q);
}

Ref reverse(Ref q) {
    if (isNil(q) != 0)
        return q;

    return appendQ(reverse((Ref)right(q)), left(q));
}

/* Question 4 */
int length(Ref list) {
    if (isNil(list) != 0) return 0;
    return 1 + length((Ref)right(list));
}

int isSorted(Ref list) {
    if (isNil(list) != 0 || isNil(right(list)) != 0)
        return 1;
    if (length((Ref)left(list)) > length((Ref)left((Ref)right(list))))
        return 0;
    return isSorted((Ref)right(list));
}

/* Question 7 */
int sameLength(Ref list1, Ref list2) {
    if (isNil(list1) != 0 && isNil(list2) != 0)
        return 1;
    if (isNil(list1) != 0 || isNil(list2) != 0)
        return 0;
    return sameLength((Ref)right(list1), (Ref)right(list2));
}
