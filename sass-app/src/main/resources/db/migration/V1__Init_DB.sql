----------------------------------------------------------
-- TABLES
----------------------------------------------------------

DROP TABLE IF EXISTS "categories" CASCADE;
CREATE TABLE "public"."categories" (
    "id" character varying(255) NOT NULL,
    "created_at" timestamp(6) NOT NULL,
    "created_by" character varying(255) NOT NULL,
    "deleted" boolean NOT NULL,
    "update_at" timestamp(6),
    "update_by" character varying(255),
    "description" text,
    "name" character varying(255) NOT NULL,
    CONSTRAINT "categories_pkey" PRIMARY KEY ("id")
)
WITH (oids = false);

CREATE UNIQUE INDEX category_name_unique_constraint ON public.categories USING btree (name);


DROP TABLE IF EXISTS "products" CASCADE;
CREATE TABLE "public"."products" (
    "id" character varying(255) NOT NULL,
    "created_at" timestamp(6) NOT NULL,
    "created_by" character varying(255) NOT NULL,
    "deleted" boolean NOT NULL,
    "update_at" timestamp(6),
    "update_by" character varying(255),
    "alert_threshold" integer NOT NULL,
    "description" text,
    "name" character varying(255) NOT NULL,
    "price" numeric(38,2) NOT NULL,
    "reference" character varying(255) NOT NULL,
    "category_id" character varying(255),
    CONSTRAINT "product_pkey" PRIMARY KEY ("id")
)
WITH (oids = false);

CREATE UNIQUE INDEX product_reference_unique_constraint ON public.products USING btree (reference);


DROP TABLE IF EXISTS "stock_mvts" CASCADE;
CREATE TABLE "public"."stock_mvts" (
    "id" character varying(255) NOT NULL,
    "created_at" timestamp(6) NOT NULL,
    "created_by" character varying(255) NOT NULL,
    "deleted" boolean NOT NULL,
    "update_at" timestamp(6),
    "update_by" character varying(255),
    "comment" text,
    "date_mvt" date NOT NULL,
    "quantity" integer NOT NULL,
    "type_mvt" character varying(255) NOT NULL,
    "product_id" character varying(255),
    CONSTRAINT "stock_mvt_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "stock_mvt_type_mvt_check" CHECK ((type_mvt)::text = ANY ((ARRAY['IN'::character varying, 'OUT'::character varying])::text[]))
)
WITH (oids = false);

----------------------------------------------------------
-- CONTRAINTES (Clés étrangères)
----------------------------------------------------------

ALTER TABLE ONLY "public"."products"
    ADD CONSTRAINT "fk_category_id"
    FOREIGN KEY (category_id) REFERENCES categories(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."stock_mvts"
    ADD CONSTRAINT "fk_product_id"
    FOREIGN KEY (product_id) REFERENCES products(id) NOT DEFERRABLE;

-- 2026-04-26 09:02:04 UTC