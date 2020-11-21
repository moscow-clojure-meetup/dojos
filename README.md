## **DOJOS Web App**

### Server

##### Start the dev server:
`lein repl`

```clojure 
(dev)
(go)
```

http://localhost:3001/api/echo should return a simple response



### Frontend

##### Start the shadow-cljs dev server:

`lein watch-shadow`

http://localhost:3000 should return the main html page

##### For production build use this command

`lein with-profile +front shadow release app --config-merge '{:closure-defines {dojos.app.config/SENTRY_DNS "https://...ingest.sentry.io/..."}}'`

where `https://...ingest.sentry.io/...` is a Sentry DNS URL
