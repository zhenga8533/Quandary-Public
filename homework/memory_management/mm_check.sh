
echo "Testing $1 for correctness...\n"

correct_count=0

output=$(../../ref/quandary -gc MarkSweep -heapsize 384 $1 1)
ret_val=$(echo "$output" | grep -oE '[0-9]+$' | tail -n 1)
if [ $ret_val -eq 5 ]; then
    echo "Test 1 (MarkSweep 384) passed! (return code $ret_val)"
    correct_count=$((correct_count+1))
else
    echo "Test 1 (MarkSweep 384) failed! (return code $ret_val)"
fi

output=$(../../ref/quandary -gc MarkSweep -heapsize 408 $1 1)
ret_val=$(echo "$output" | grep -oE '[0-9]+$' | tail -n 1)
if [ $ret_val -eq 0 ]; then
    echo "Test 1 (MarkSweep 408) passed! (return code $ret_val)"
    correct_count=$((correct_count+1))
else
    echo "Test 1 (MarkSweep 408) failed! (return code $ret_val)"
fi

output=$(../../ref/quandary -gc MarkSweep -heapsize 384 $1 2)
ret_val=$(echo "$output" | grep -oE '[0-9]+$' | tail -n 1)
if [ $ret_val -eq 0 ]; then
    echo "Test 2 (MarkSweep 384) passed! (return code $ret_val)"
    correct_count=$((correct_count+1))
else
    echo "Test 2 (MarkSweep 384) failed! (return code $ret_val)"
fi


output=$(../../ref/quandary -gc RefCount -heapsize 384 $1 2)
ret_val=$(echo "$output" | grep -oE '[0-9]+$' | tail -n 1)
if [ $ret_val -eq 5 ]; then
    echo "Test 2 (RefCount 384)  passed! (return code $ret_val)"
    correct_count=$((correct_count+1))
else
    echo "Test 2 (RefCount 384)  failed! (return code $ret_val)"
fi

output=$(../../ref/quandary -gc MarkSweep -heapsize 384 $1 3)
ret_val=$(echo "$output" | grep -oE '[0-9]+$' | tail -n 1)
if [ $ret_val -eq 5 ]; then
    echo "Test 3 (MarkSweep 384) passed! (return code $ret_val)"
    correct_count=$((correct_count+1))
else
    echo "Test 3 (MarkSweep 384) failed! (return code $ret_val)"
fi

output=$(../../ref/quandary -gc Explicit -heapsize 384 $1 3)
ret_val=$(echo "$output" | grep -oE '[0-9]+$' | tail -n 1)
if [ $ret_val -eq 0 ]; then
    echo "Test 3 (Explicit 384)  passed! (return code $ret_val)"
    correct_count=$((correct_count+1))
else
    echo "Test 3 (Explicit 384)  failed! (return code $ret_val)"
fi

output=$(../../ref/quandary -gc Explicit -heapsize 384 $1 4)
ret_val=$(echo "$output" | grep -oE '[0-9]+$' | tail -n 1)
if [ $ret_val -eq 5 ]; then
    echo "Test 4 (Explicit 384)  passed! (return code $ret_val)"
    correct_count=$((correct_count+1))
else
    echo "Test 4 (Explicit 384)  failed! (return code $ret_val)"
fi

output=$(../../ref/quandary -gc MarkSweep -heapsize 384 $1 4)
ret_val=$(echo "$output" | grep -oE '[0-9]+$' | tail -n 1)
if [ $ret_val -eq 0 ]; then
    echo "Test 4 (MarkSweep 384) passed! (return code $ret_val)"
    correct_count=$((correct_count+1))
else
    echo "Test 4 (MarkSweep 384) failed! (return code $ret_val)"
fi

if [ $correct_count -eq 8 ]; then
    echo "\nAll tests passed!"
else
    echo "\n$((8-correct_count)) tests failed!"
fi