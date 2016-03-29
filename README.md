# democracy-works-exercise

I've included some notes on how I did things, choices I made, etc.

But first: in order to get this up and running, you'll need to start the server and compile the ClojureScript.  The short version is:

0) This assumes you have a current version of leiningen installed.
1) Start up a repl in the project, and run the `go` function in the repl.
1a) If you haven't started up the system before, additionally run the `run-migrations` function. Running `go` without a database will warn you of this.
2) From the command-line, at the root of the project dir run `lein cljsbuild once` (ignore cljsbuild config warnings--apparently from the presence of the project in the checkouts dir, but doesn't affect functionality).

Now you should be able to go to http://localhost:3000 to see the app in action.

NOTE: the REPL dev environment utils used to run the server and migrations depends on the :dev profile being part of leiningen defaults, per the default configuration--if you have overridden this to remove :dev it will not work without specifying the dev profile when starting a REPL (I'm guessing most people have not but figured I'd mention this just in case).

In more detail:

## How to run the server

The application is set up to be run from the repl.  You can start a repl using `lein repl` or use cider, as I do.  The dev profile should be loaded by default, then you can start using the `go` helper function.

Note that on the first time you run it, it will complain about the missing database--you can run the migrations very easily as shown below:

```clojure
democracy-works-exercise.dev> (go)
;; Starting SqliteComponent
Looks like your migrations may not have been run. You can run these using the `democracy-works-exercise.dev/run-migrations` helper function.
;; Starting AlephComponent
#<SystemMap>
democracy-works-exercise.dev> (run-migrations)
Applying 001-todos
Applying 002-dummy-todos
Applying 003-todo-lists
nil
democracy-works-exercise.dev> 
```

If you want to stop or restart the server there are helpers for that as well:

```
democracy-works-exercise.dev> (go)
;; Starting SqliteComponent
;; Starting AlephComponent
#<SystemMap>
democracy-works-exercise.dev> (stop)
;; Stopping AlephComponent
#<SystemMap>
democracy-works-exercise.dev> (go)
;; Starting SqliteComponent
;; Starting AlephComponent
#<SystemMap>
democracy-works-exercise.dev> (reset)
;; Stopping AlephComponent
;; <snip>
;; Starting SqliteComponent
;; Starting AlephComponent
#<SystemMap>
democracy-works-exercise.dev> 
```

Etc.

## Getting the ClojureScript going:

The usual way with `lein-cljsbuild`:

```bash
$ lein cljsbuild auto
```

Ignore the warning about the cljsbuild config--I believe that is because of the addition of a checkouts directory, as it only started appearing after I added that in step 10.  Otherwise, not much to add here.


## Notes on various steps:

* Step 1 - I started using a compojure template since I have used the library a ton, it is simple and well-supported, and it takes care of a lot of boilerplate.

* Step 2 - see step 1--I cheated a bit here in the sense that I didn't see the point of *not* using the compojure lein template, since it is so basic.  P.S. this under construction gif was one of the weirdest I found...love the dancing carrot!

* Step 3 - I tried to make a sort of skeleton for some kind of templating system, anticipating we would want that later when integrating hiccup.

* Step 4 - at this step I took some time to flesh out more infrastructure--part of this was simply for fun, as I am still learning about component and wanted to try using it more, and some of this was because I was unfamiliar with SQLite and wanted to give that a shot too.  Turns out boolean values are represented as integers (0/1) in SQLite, which I don't much care for--so I spent a little time here wrapping that up a bit so I could deal strictly with boolean values on the Clojure side, but have these translated through to the proper integer values when inserting or querying the SQLite database (see `democracy-works-exercise.sqlite.core`).  I also added the ragtime migration library just because I find having any kind of migrations vs. not makes my life a lot easier.  Finally, I made sure my REPL dev environment was a bit fleshed out so I could eliminate some drudgery (see `democracy-works-exercise.dev`).

Note that from this step on, you should be able to start up the entire system by loading up a repl and simply calling 'go'.  It will warn you if the SQLite migrations have not been run or not.

One last note--for 4.ii, I chose to use boolean values for the todo/done state, expecting I would represent those as keywords elsewhere as need be.

* Steps 5 - 8 --not much to add here.  These were relatively simple, and I took the path of least resistance by simply adding route handlers for each action with a corresponding database call.  Things only got complicated at...

* Step 9 - required a bit more work to make sure we were passing the right information around, and the SQLite migration was a bit tricky--having never used it before I didn't realize how limited it is compared to what I'm more familiar with (PostgreSQL), so I did the simplest thing possible to get it working.  This is the step I also learned how to put more than one statement in a ragtime migration...

* Step 10 and beyond - here I moved the templating almost entirely to the client, turning the system into a single-page app.  While I may have done this differently had I started from a different place, I chose to simply push mutative updates to the server via AJAX, adjusted the routes to be a bit more RESTful (but not perfectly so), and created a very simple websocket mechanism to push updates back to the client (sort of a Flux-like architecture).  Using a simple pub/sub mechanism it distributes changes to all clients simultaneously, but it's very primitive.  This was an opportunity to play with reagent which I haven't really touched, as well as aleph and manifold on the backend.

I should add that I discovered that lein-transit has a bug and does not extract the content-type from a request properly.  I've included a checkouts directory with a modified version of lein-transit so that the app works properly.  Once the interview period is over I'll make a PR for this.

## Next steps (were I to continue working on this):

  - I would have liked to set up some security and restrict accounts, but that's probably another evening of futzing about for me, and tomorrow is Tuesday, so I stopped here.

  - I hacked together a bit of CSS just so it's not horribly ugly and terrible UX-wise, but it's still quite bad.  I would spend more time on this if I could, but UX/design is certainly not my strong suit.

  - Tests--I made a pretty deliberate decision to forgo tests from the get-go.  Testing is something I otherwise do consistently, but I didn't add any tests here simply because it would have slowed me down and I did end up doing a lot of "repl-driven testing."  I spent more time building out infrastructural elements like the component setup.  However, in a production system it's worth the time over the lifetime of an application, and I always insist on testing.

  - Setting up the app to actually run in a production environment.  Right now it is entirely contained within the component framework, and I didn't add any core namespace or functionality to load this app up from a jar or whatnot.

  - Figure out what the websocket disconnect I get on page reload is.  Doesn't seem to affect functionality but it's annoying.

  - More safety: catching potential edge-cases, etc...there's a lot of places this code could go wrong that I'm not taking into account.  Pretty much happy-paths everywhere.

  - Add some client-side routing using something like secretary--I didn't bother moving the about page to use client-side rendering, but I would need to set up something like secretary before I did so.

In general it was challenging figuring out how to do "as much good work as you can on each step rather than trying to do a little work on more steps," as I was anticipating later steps much of the time--I think I certainly had it in the back of my mind the entire time that I'd be moving to rendering on the client-side, for example, so that changed my approach a bit.  But I did take a fair amount of time to try and build a solid base so that later steps were easier, so I think I did mostly follow this precept appropriately.

In any case--I'd love to hear feedback if possible, whenever.
