(ns datomicpro-test.core)

(comment
  (require '[datomic.api :as d])

  ;; create the connection string
  ;; see docs https://docs.datomic.com/pro/clojure/index.html#datomic.api/connect
  (def db-uri "datomic:ddb-local://localhost:8000/datomicpro-test/foo?aws_access_key_id=dummy&aws_secret_key=dummy")

  ;; create database `foo` as specified in the `db-uri`
  (d/create-database db-uri)

  ;; list databases
  ;; note how the uri is the same, except the database part is replaced by `*`
  (d/get-database-names "datomic:ddb-local://localhost:8000/datomicpro-test/*?aws_access_key_id=dummy&aws_secret_key=dummy")

  (def movie-schema [{:db/ident :movie/title
                      :db/valueType :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc "The title of the movie"}

                     {:db/ident :movie/genre
                      :db/valueType :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc "The genre of the movie"}

                     {:db/ident :movie/release-year
                      :db/valueType :db.type/long
                      :db/cardinality :db.cardinality/one
                      :db/doc "The year the movie was released in theaters"}])

  ;; create a connection
  (def conn (d/connect db-uri))

  ;; transacts the schema. Returns a completed future.
  @(d/transact conn movie-schema)

  ;; sample movie data
  (def first-movies [{:movie/title "The Goonies"
                      :movie/genre "action/adventure"
                      :movie/release-year 1985}
                     {:movie/title "Commando"
                      :movie/genre "action/adventure"
                      :movie/release-year 1985}
                     {:movie/title "Repo Man"
                      :movie/genre "punk dystopia"
                      :movie/release-year 1984}])

  ;; transact movies
  @(d/transact conn first-movies)

  ;; get the current database
  (def db (d/db conn))

  ;; all data from 1985 query
  (def all-data-from-1985 '[:find ?title ?year ?genre
                            :where [?e :movie/title ?title]
                            [?e :movie/release-year ?year]
                            [?e :movie/genre ?genre]
                            [?e :movie/release-year 1985]])

  (d/q all-data-from-1985 db)
  ;; => #{["The Goonies" 1985 "action/adventure"] ["Commando" 1985 "action/adventure"]}

  ;; get id of the `Commando` movie
  (def commando-id
    (ffirst (d/q '[:find ?e
                   :where [?e :movie/title "Commando"]]
                 db)))

  ;; update the genre of `Commando`
  @(d/transact conn [{:db/id commando-id :movie/genre "future governor"}])

  ;; get a new db
  (def updated-db (d/db conn))

  (d/q all-data-from-1985 updated-db)
  ;; => #{["The Goonies" 1985 "action/adventure"] ["Commando" 1985 "future governor"]}

  ;; you can still query the previous database
  (d/q all-data-from-1985 db)
  ;; => #{["The Goonies" 1985 "action/adventure"] ["Commando" 1985 "action/adventure"]}
  )
