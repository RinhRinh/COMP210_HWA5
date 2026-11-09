# A5: Shakespeare's Favorite Words

**COMP210, Fall 2026**

| | |
|---|---|
| Released | Tuesday, November 3 |
| Due | Tuesday, November 10, 5:00pm |
| Points | 115 |
| Collaboration | Follows the syllabus policy. Never share your API key. |

Shakespeare's complete works, plays and poems together, run 884,647 words by
Marvin Spevack's count. Which ones did he lean on? In A5 you count every word
in one of his plays and pull out the 20 he used most. A hash table counts the
words, and a max-heap picks the winners.

BRIDGES serves the full text of all 35 plays, so you can run your code on
Hamlet, Macbeth, or any other play you like. Your table and your heap also
show up in the BRIDGES web view, chains and all.

This assignment adapts BRIDGES assignment #8, "Shakespeare Favorite Words,"
from UNC Charlotte.

## Counting with a Dictionary

A dictionary (the Map ADT from L14) stores key/value pairs. Here each key is
a word and its value is how many times that word has appeared so far.
Counting a whole play takes one loop:

```
for each word w in the play:
    count = d.get(w)
    if count is null:
        d.set(w, 1)
    else:
        d.set(w, count + 1)
```

`Dictionary.java` is the interface: `get`, `set`, `size`, and a for-each loop
that hands you each pair as a `Map.Entry` (`e.getKey()` is the word,
`e.getValue()` the count). `StandardDictionary.java` implements it with
`java.util.HashMap`. You build your own implementation, `HashTable`.

## Part 1: `HashTable` (40 points)

Open `src/main/java/comp210/a5/HashTable.java`. It uses separate chaining,
the version from the hashing lectures: an array of buckets, where each bucket
holds a linked chain of `Node`s. The fields, the `Node` class, `capacity()`,
`longestChain()`, and the drawing code are written. You write five pieces:

| Method | What it does |
|---|---|
| `indexFor(key, capacity)` | picks a bucket from 0 to capacity - 1 using `key.hashCode()` |
| `get(key)` | walks one chain and returns the count, or `null` if the word is absent |
| `set(key, value)` | replaces the count if the word is in its chain, otherwise adds a new node |
| `resize(newCapacity)` | moves every node into a new, bigger array |
| `HashTableIterator` | hands out every entry once, bucket by bucket (see "What an iterator is" below) |

The rules:

- The table starts with 16 buckets. When adding a new word pushes
  `count / table.length` above `maxLoadFactor` (0.75 by default), `set` calls
  `resize(2 * table.length)`. Careful: `count` and `table.length` are both
  `int`s, so `count / table.length` in Java is integer division (`12 / 16`
  is `0`). Cast one side to `double` before you divide.
- `hashCode()` can be negative, and Java's `%` keeps the sign: `-7 % 4` is
  `-3`, which is not a bucket. `Math.floorMod(a, b)` lands in 0 to b - 1 every
  time. `Math.abs` has a trap of its own: one string,
  `"polygenelubricants"`, has a hash code of `Integer.MIN_VALUE`, and
  `Math.abs(Integer.MIN_VALUE)` is still negative. The autograder tries it.
- `set(null, ...)` and `set(word, null)` throw `IllegalArgumentException`.
  `get(null)` returns `null`.
- Build the chains from your own `Node`s. No `HashMap`, `ArrayList`,
  `LinkedList`, or other `java.util` collections inside `HashTable`. The
  autograder flags these for your instructor to review by hand.

The bug that catches the most people is in `resize`. A word's bucket depends
on the capacity, so when the array doubles, most words belong in a different
bucket than before. Copying each chain to the same index in the new array
leaves words where `get` will never look. Run every node back through
`indexFor` with the new capacity.

### What an iterator is, and why `HashTable` needs one

You have been writing for-each loops since Module 1:

```java
for (Map.Entry<String, Integer> e : counts) {
    System.out.println(e.getKey() + " " + e.getValue());
}
```

For an array, Java knows how to walk the elements. For your own class, it
does not, so the class has to supply an **iterator**: an object that walks
through a collection one element at a time and remembers where it stopped.
Java rewrites the loop above into calls on that object:

```java
Iterator<Map.Entry<String, Integer>> it = counts.iterator();
while (it.hasNext()) {
    Map.Entry<String, Integer> e = it.next();
    System.out.println(e.getKey() + " " + e.getValue());
}
```

So an iterator has two jobs:

| Method | Its job |
|---|---|
| `hasNext()` | Answers "is there anything left?" It only looks. Calling it twice in a row gives the same answer and moves nothing. |
| `next()` | Hands back the current element and moves the bookmark forward to the one after it. If nothing is left, it throws `NoSuchElementException`. |

Think of the iterator as a bookmark. The constructor puts the bookmark on the
first element, `next()` reads the element under the bookmark and slides it
forward, and `hasNext()` checks whether the bookmark has run off the end.

`Dictionary` promises for-each loops (it `extends Iterable`), and
`iterator()` at the bottom of `HashTable.java` is already written: it
returns `new HashTableIterator()`. Your job is the inside of
`HashTableIterator`. It gets two fields as its bookmark:

- `bucket`, the index of the bucket the bookmark is in
- `current`, the next `Node` to hand out, or `null` once every node is out

The walk goes bucket 0's chain from front to back, then bucket 1's chain, and
so on, skipping empty buckets. Here is a 4-bucket table and the bookmark after
each call:

```
bucket 0: (empty)
bucket 1: "the" -> "to"
bucket 2: (empty)
bucket 3: "and"

after the constructor:  bucket 1, current = "the"
next() returns "the":   bucket 1, current = "to"
next() returns "to":    bucket 3, current = "and"    (bucket 2 skipped)
next() returns "and":   current = null              hasNext() is now false
```

Notice the second call: when a chain runs out, `next()` has to find the next
non-empty bucket before it returns, so the bookmark is always sitting on a
real node or on `null`. The constructor does the same search starting from
bucket 0. `next()` hands each entry out as
`new AbstractMap.SimpleEntry<>(key, value)`.

You never call your iterator by name. `WordStats.topWords` loops over
`counts` with for-each, and that loop uses it. If the iterator skips words,
your top 20 comes out wrong or empty. The starter test
`iteratorVisitsEveryWordOnce` checks this: it puts eight words in a
`new HashTable(4, 1000.0)` (several words per chain, some empty buckets),
loops over it with for-each, and checks that every word shows up exactly
once. Write a few more tests like it with other words and capacities.

## Part 2: `MaxHeap` (40 points)

Open `MaxHeap.java`. This is the array heap from L18, keyed so the largest
key sits at `heap[0]`. The entry at index i has its children at 2i + 1 and
2i + 2 and its parent at (i - 1) / 2. `peek`, `size`, `isEmpty`, `swap`,
`ensureRoom`, and the drawing code are written. You write five methods:

| Method | What it does |
|---|---|
| `insert(key, value)` | puts the entry at `heap[size]` and percolates it up |
| `pop()` | removes `heap[0]`, moves the last entry to the root, and percolates it down |
| `percolateUp(i)` | swaps upward while the entry is larger than its parent |
| `percolateDown(i)` | swaps with the larger child while that child is larger |
| `buildTree(i)` | turns the array into BRIDGES tree nodes so the heap can be drawn |

The rules:

- `pop` and `peek` on an empty heap throw `NoSuchElementException`.
- `insert` with a `null` key throws `IllegalArgumentException`.
- `insert` and `pop` run in O(log n). The autograder pushes 200,000 entries
  through your heap in under 3 seconds to check.
- No `PriorityQueue`, no sorting, and no `java.util` collections inside
  `MaxHeap`. Same flag as Part 1.

`buildTree` is the short one. BRIDGES draws trees out of linked
`BinTreeElement` nodes, not arrays. For index i, make a node holding
`heap[i].getValue()` with the one-argument constructor,
`new BinTreeElement<>(value)`, give it `setLabel(label(i))`, and attach the trees you
build from the two child indexes with `setLeft` and `setRight`. Past the last
entry, return `null`. Each call uses the same 2i + 1 and 2i + 2 arithmetic as
`percolateDown`, so the picture BRIDGES draws matches the tree you drew in
L18.

## Part 3: `WordStats` (20 points)

Open `WordStats.java`. `tokenize` is written: it splits a play into lowercase
words and trims the punctuation, so "Ophelia!" becomes "ophelia". You write
two methods:

- `countWords(words, counts)` runs the counting loop above. Use only `get`
  and `set`, so it works on any `Dictionary`.
- `topWords(counts, k)` returns the k most frequent words, largest count
  first, as (word, count) entries. Insert every entry of `counts` into a
  `MaxHeap<Integer, String>` keyed on the count, then pop k times. If
  `counts` holds fewer than k words, return all of them.

The heap's key is the count and its value is the word, the reverse of the
dictionary. Flip each pair on the way in and again on the way out.

## Part 4: Run it (3 points)

1. Follow the Setup section below if you have not already.
2. Open `ShakespeareWords.java` and set `WORK` to part of a play's title. It
   starts as `"hamlet"`. Pick any play you like; a title that matches nothing
   prints the full list.
3. Click the green arrow next to `main`.

The program prints the play's word count, the number of distinct words, your
table's final capacity and longest chain, a check against `java.util.HashMap`,
and the top 20 words. Then it sends two pictures to BRIDGES:

- a small `HashTable` with 8 buckets and the play's first 20 distinct words,
  so you can see the chains
- the 15 most frequent words in your `MaxHeap`, drawn by `buildTree`

The heap picture shows each node as a dot. Hover over a dot to see its word
and count, and scroll to zoom.

Your pictures live at `https://bridges-cs.herokuapp.com/assignments/5/yourusername`.
Put that URL at the top of your write-up; it is graded with the write-up. If
the check against `HashMap` says NO, the program lists the first word that
disagrees.

## Part 5: Write-up (12 points)

Answer the three questions in `writeup.md` using the output from your play.
A few sentences each, with numbers where the question asks for them. The
write-up goes to its own Gradescope assignment, separate from your code; see
What to submit.

## Extra credit (+10, plus the early bonus)

Each of these is worth 5 points, and the autograder grades both. They are
optional.

**A BST dictionary.** `BST.java` is a second `Dictionary`, built as a binary
search tree (L17) out of BRIDGES `BSTElement` nodes. Write `get`, `set`, and
an iterator that walks the words in alphabetical order, with the same
`hasNext`/`next` jobs as the `HashTable` one. Then try it in
`ShakespeareWords`: change `HashTable counts = new HashTable();` to
`Dictionary<String, Integer> counts = new BST();`, and comment out the two
lines that print `counts.capacity()` and `counts.longestChain()`, since a BST
has no buckets. Run it and check that the top words come out the same, then
put `ShakespeareWords` back the way it was. `get(null)` returns `null` here
too. One trap: make a leaf with
`new BSTElement<>(key, value, null, null)`. The two-argument constructor
treats a String key as a display label, and `getKey()` comes back `null`.

**The original BRIDGES heap.** `PointerMaxHeap.java` is the same max-heap
built from linked nodes instead of an array, which is how the original BRIDGES
assignment does it. With no array, `insert` has no `heap[size]` to aim for, so
each node stores the sizes of its two subheaps and each insert heads toward
the smaller one. The comments in the file give the algorithm for `insert` and
`pop`. Once both heaps work, compare how each one finds the spot for a new
entry: `MaxHeap` goes straight to `heap[size]`, while `PointerMaxHeap` keeps
two size counts in every node to steer. That bookkeeping is why L18
stores heaps in arrays.

## Setup

Same setup as A0 and A4: a Maven project you open in IntelliJ, with BRIDGES
pulled from JitPack and your key read from `.env` by `BridgesConfig`.

1. **File > Open** the folder you cloned, the one holding `pom.xml`. Wait for
   the Maven import to finish.
2. Copy `.env.example` to `.env` in the same folder and fill in the same
   username and API key you used in A0. Leave `BRIDGES_ASSIGNMENT` at 5.
3. You are ready to write code and run `ShakespeareWords`.

If the import or the key gives you trouble, the "When it breaks" list in A0's
`code-along.md` covers it.

## Testing your code

`src/test/java/comp210/a5/StarterTest.java` has nine tests, including one
each for the iterator, `resize`, and `buildTree`. Right-click the
file and choose **Run 'StarterTest'**. Gradescope runs 23 tests on the
required parts, including collisions, resizing, negative hash codes, big
heaps, and timing.
Passing the starter tests is a good sign and not a guarantee, so add tests of
your own. A table built with `new HashTable(1, 1000.0)` puts every word in one
chain, which makes collisions easy to test.

## What to submit

A5 has two Gradescope assignments, the same setup as the A1 to A3
worksheets.

Upload your code to **A5**:

1. `HashTable.java`
2. `MaxHeap.java`
3. `WordStats.java`
4. For extra credit: `BST.java`, `PointerMaxHeap.java`, or both

The autograder runs as soon as you submit, and you can resubmit as many times
as you like before the deadline. An extra credit file that does not compile is
graded as the starter version, so it cannot cost you the required parts.

Upload `writeup.md`, or a PDF of it, to **A5 Write-up**, with your BRIDGES URL
at the top. Your instructor grades it by hand.

Leave `.env` out of both. Same rule as A0: an API key anywhere in either
submission is an automatic zero, and the autograder scans every file you
upload for one.

## Grading

| Part | Points | Gradescope assignment | Graded by |
|---|---|---|---|
| `HashTable` | 40 | A5 | autograder |
| `MaxHeap` | 40 | A5 | autograder |
| `WordStats` | 20 | A5 | autograder |
| **Extra credit:** `BST` | +5 | A5 | autograder |
| **Extra credit:** `PointerMaxHeap` | +5 | A5 | autograder |
| **Extra credit:** submit at least 72 hours before the deadline | +10 | A5 | autograder |
| BRIDGES URL shows both pictures under your account | 3 | A5 Write-up | instructor |
| Write-up questions | 12 | A5 Write-up | instructor |

The early bonus goes by the time of your code submission to A5. Each
submission is checked on its own, so a late resubmission does not earn it,
even if an earlier one did.

## Files

```
your cloned folder/          <- open THIS folder in IntelliJ
  README.md                   this file
  writeup.md                  Part 5
  pom.xml                     Maven build, pulls BRIDGES, dotenv-java, JUnit
  .env.example                template for your credentials
  .gitignore                  keeps .env out of git
  src/main/java/comp210/a5/
    HashTable.java            Part 1, yours
    MaxHeap.java              Part 2, yours
    WordStats.java            Part 3, yours
    ShakespeareWords.java     Part 4; WORK picks the play
    Dictionary.java           the interface both dictionaries implement
    StandardDictionary.java   Dictionary on java.util.HashMap, for checking
    BST.java                  extra credit
    PointerMaxHeap.java       extra credit
    BridgesConfig.java        reads .env, same as A0
  src/test/java/comp210/a5/
    StarterTest.java          nine tests to start from
```
