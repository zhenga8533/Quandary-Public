Ref main(int arg) {
    Ref list1 = (3 . (4 . nil));
    Ref list2 = ((56 . (5 . nil)) . nil) . (26 . (2 . ((8 . nil) . nil)));
    
    Ref result = append(list1, list2);
    return result;
}

Ref append(Ref list1, Ref list2) {
    if (isNil(list1) == 1)
        return list2;
    return left(list1) . append((Ref)right(list1), list2);
}
