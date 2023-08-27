(ns datomicpro-test.core)

(comment

  (require '[datomic.api :as d])

  ;; create the connection string
  ;; see docs https://docs.datomic.com/pro/javadoc/datomic/Peer.html#connect-java.lang.Object-
  (def db-uri "datomic:ddb-local://localhost:8000/datomicpro-test/hello?aws_access_key_id=dummy&aws_secret_key=dummy")

  (d/get-database-names "datomic:ddb-local://localhost:8000/datomicpro-test/*?aws_access_key_id=dummy&aws_secret_key=dummy")

  ;; create database
  (d/create-database db-uri)

  ;; create a connection
  (def conn (d/connect db-uri))

  ;; send a sample transaction
  @(d/transact conn [{:db/doc "Hello world"}])

  )
