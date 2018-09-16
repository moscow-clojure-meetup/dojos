## **DOJO Tools**

#### Probably you will need to install shadow-cljs

```
    npm i -g shadow-cljs
```

#### Then run dev build

```
    npm run dev
```

#### Open http://localhost:3000 and enjoy


#### Connect to repl

nRepl server will listen on 3333 port

When you connect run this

```clojure
(shadow.cljs.devtools.api/nrepl-select :app)
```
