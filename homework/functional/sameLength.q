int main(int arg) {
    Ref list1 = (3 . (5 . (5 . nil))) . ((2 . (8 . nil)) . ((6 . (7 . (4 . nil))) 
                . ((2 . (3 . (56 . (92 . nil))) . nil))));
    Ref list2 = (3 . (5 . nil)) . ((2 . (8 . nil)) . ((6 . (7 . (4 . nil))) .
                ((2 . (3 . (56 . (92 . nil))) . nil))));

    int result = sameLength(list1, list2);
    return result;
}

int sameLength(Ref list1, Ref list2) {
    if (isNil(list1) != 0 && isNil(list2) != 0)
        return 1;
    if (isNil(list1) != 0 || isNil(list2) != 0)
        return 0;
    return sameLength((Ref)right(list1), (Ref)right(list2));
}
