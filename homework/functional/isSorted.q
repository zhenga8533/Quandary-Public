int main(int arg) {
    Ref list1 = (3 . (5 . (5 . nil))) . ((2 . (8 . nil)) . ((6 . (7 . (4 . nil))) 
                . ((2 . (3 . (56 . (92 . nil))) . nil))));
    Ref list2 = (3 . (5 . nil)) . ((2 . (8 . nil)) . ((6 . (7 . (4 . nil))) .
                ((2 . (3 . (56 . (92 . nil))) . nil))));

    int result = isSorted(list2);
    return result;
}

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
