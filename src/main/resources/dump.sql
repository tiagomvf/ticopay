--
-- PostgreSQL database dump
--

-- Dumped from database version 14.12 (Debian 14.12-1.pgdg120+1)
-- Dumped by pg_dump version 15.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: quarkus
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO quarkus;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: transfer; Type: TABLE; Schema: public; Owner: quarkus
--

CREATE TABLE public.transfer (
    value numeric(38,2),
    id bigint NOT NULL,
    payee bigint,
    payer bigint
);


ALTER TABLE public.transfer OWNER TO quarkus;

--
-- Name: transfer_seq; Type: SEQUENCE; Schema: public; Owner: quarkus
--

CREATE SEQUENCE public.transfer_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.transfer_seq OWNER TO quarkus;

--
-- Name: wallet; Type: TABLE; Schema: public; Owner: quarkus
--

CREATE TABLE public.wallet (
    balance numeric(38,2),
    ownerid bigint NOT NULL
);


ALTER TABLE public.wallet OWNER TO quarkus;

--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: quarkus
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--


insert INTO public.wallet (balance, ownerid) values (1000,4);
insert INTO public.wallet (balance, ownerid) values (0,15);

