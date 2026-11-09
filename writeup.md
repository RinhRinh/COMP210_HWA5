# A5 Write-up

**Name:**
**Onyen:**
**BRIDGES URL:** https://bridges-cs.herokuapp.com/assignments/5/
**Play I counted (the title ShakespeareWords printed):**

Three questions, 12 points. A few sentences each. Use the numbers your
program printed for your play, and where a question asks for a number, show
where it came from.

---

## Question 1: The top 20 (4 points)

Paste your top 20 list.

A few entries may not be words anyone says out loud. In Hamlet, "ham" makes
the list: the script puts "Ham." in front of each of Hamlet's speeches, and
`tokenize` turns that into the word "ham". Adding
`System.out.println(play.getText());` to `main` shows you the script your
program counted. Find any entries like that in your list and say where they
come from. If you have none, name the entry nearest the top that surprised
you and explain it.

Most of the rest are short words like "the" and "and." Suppose you wanted the
top 20 to say something about your play, so you kept a list of a few hundred
words to skip. Where in the code would you check that list? What would you
store it in so that each check costs O(1) on average?

```

```

---

## Question 2: The cost of topWords (4 points)

Let n be the number of distinct words in your play and k = 20.

Your `topWords` does n inserts and then k pops. Give its Big-O in terms of n
and k. Then use your play's n to estimate, roughly, the most swaps the
inserts could make and the most swaps the pops could make.

Sorting all n words by count would cost O(n log n). Your inserts alone cost
that much. Name one change to `topWords` that brings the cost for k = 20
below O(n log n), and give the new Big-O.

```

```

---

## Question 3: Resizing (4 points)

Your table started with 16 buckets and a maximum load factor of 0.75. From
your program's output: how many buckets does it have after counting? Work
out how many times `resize` ran and what the load factor is now.

Now suppose `resize` never ran and the table stayed at 16 buckets. About how
long would the average chain be once every word was in? `countWords` calls
`get` and `set` once for each word in the play, and each call walks a chain.
Roughly how many nodes would those walks visit for your play, compared with
the table that resized? For a rough answer, use the average chain length you
just found and assume each walk covers about half of a chain.

```

```
