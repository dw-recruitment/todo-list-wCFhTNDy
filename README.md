# democracy-works-exercise


Notes on various steps:

* Step 1 - I started using a compojure template since I have used the library a ton, it is simple and well-supported, and it takes care of a lot of boilerplate.

* Step 2 - see step 1--I cheated a bit here in the sense that I didn't see the point of *not* using the compojure lein template, since it is so basic.  P.S. this under construction gif was one of the weirdest I found...love the dancing carrot!

* Step 3 - I tried to make a sort of skeleton for some kind of templating system, anticipating we would want that later when integrating hiccup.

* Step 4 - at this step I took some time to flesh out more infrastructure--part of this was simply for fun, as I am still learning about component and wanted to try using it more, and some of this was because I was unfamiliar with SQLite and wanted to give that a shot too.  Turns out boolean values are represented as integers (0/1) in SQLite, which I don't much care for--so I spent a little time here wrapping that up a bit so I could deal strictly with boolean values on the Clojure side, but have these translated through to the proper integer values when inserting or querying the SQLite database (see `democracy-works-exercise.sqlite.core`).  I also added the ragtime migration library just because I find having any kind of migrations vs. not makes my life a lot easier.  Finally, I made sure my REPL dev environment was a bit fleshed out so I could eliminate some drudgery (see `democracy-works-exercise.dev`).

Note that from this step on, you should be able to start up the entire system by loading up a repl and simply calling 'go'.  It will warn you if the SQLite migrations have not been run or not.

One last note--for 4.ii, I chose to use boolean values for the todo/done state, expecting I would represent those as keywords elsewhere as need be.

* Step 5 - 




## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server
