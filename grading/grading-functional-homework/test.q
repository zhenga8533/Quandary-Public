/* Question 1 */
int isList(Q q) {
    if (isNil(q) != 0)
        return 1;
    if (isAtom(q) != 0)
        return 0;
        
    return isList(right((Ref)q));
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


int genericEquals(Q item1, Q item2) {
    if (isNil(item1) != isNil(item2)) {
        return 0;
    } else {
        if (isNil(item1) == 1) {
            return 1;
        }
    }
    if (isAtom(item1) != isAtom(item2)) {
        return 0;
    } else {
        if (isAtom(item1) == 1) {
            if ((int)item1 == (int)item2) { /* ??? */
                return 1;
            } else {
                return 0;
            }
        }
    }
    /* item1 and item2 are Ref's */
    if (genericEquals(left((Ref)item1), left((Ref)item2)) == 1 && genericEquals(right((Ref)item1), right((Ref)item2)) == 1) {
        return 1;
    }
    return 0;
}



int main(int arg) {
    Ref input = (nil . ((314 . nil) . ((15 . nil) . ((926 . (535 . (89 . (79 . nil)))) . ((3 . (2 . (3 . (8 . (4 . nil))))) . nil))))); /* Complicated example */
    if (isSorted(input) != 0) {
        return 1;
    }
    return 0;
}

