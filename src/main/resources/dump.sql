
--
-- PostgreSQL database dump
--

-- Dumped from database version 15.7 (Debian 15.7-1.pgdg120+1)
-- Dumped by pg_dump version 15.6 (Debian 15.6-0+deb12u1)

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: actor; Type: TABLE; Schema: public; Owner: quarkus
--

CREATE TABLE public.actor (
    id bigint NOT NULL,
    tipo character varying(31) NOT NULL,
    cpf character varying(255),
    email character varying(255),
    name character varying(255),
    password character varying(255)
);


ALTER TABLE public.actor OWNER TO quarkus;

--
-- Name: actor_seq; Type: SEQUENCE; Schema: public; Owner: quarkus
--

CREATE SEQUENCE public.actor_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.actor_seq OWNER TO quarkus;

--
-- Name: notification; Type: TABLE; Schema: public; Owner: quarkus
--

CREATE TABLE public.notification (
    status smallint NOT NULL,
    value numeric(38,2) NOT NULL,
    payee bigint NOT NULL,
    payer bigint NOT NULL,
    id uuid NOT NULL,
    CONSTRAINT notification_status_check CHECK (((status >= 0) AND (status <= 2)))
);


ALTER TABLE public.notification OWNER TO quarkus;

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
-- Name: actor actor_pkey; Type: CONSTRAINT; Schema: public; Owner: quarkus
--

ALTER TABLE ONLY public.actor
    ADD CONSTRAINT actor_pkey PRIMARY KEY (id);


--
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: public; Owner: quarkus
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- Name: transfer transfer_pkey; Type: CONSTRAINT; Schema: public; Owner: quarkus
--

ALTER TABLE ONLY public.transfer
    ADD CONSTRAINT transfer_pkey PRIMARY KEY (id);


--
-- Name: wallet wallet_pkey; Type: CONSTRAINT; Schema: public; Owner: quarkus
--

ALTER TABLE ONLY public.wallet
    ADD CONSTRAINT wallet_pkey PRIMARY KEY (ownerid);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: quarkus
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

-- insert into public.actor(id, tipo, cpf, email, name, password) VALUES (1,"user", )


-- insert INTO public.wallet (balance, ownerid) values (1000,4);
-- insert INTO public.wallet (balance, ownerid) values (0,15);
