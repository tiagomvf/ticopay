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
    payee bigint NOT NULL,
    payer bigint NOT NULL
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
-- Name: transfer fk5bs33nh5s0x2l89nnhp7ex1c8; Type: FK CONSTRAINT; Schema: public; Owner: quarkus
--

ALTER TABLE ONLY public.transfer
    ADD CONSTRAINT fk5bs33nh5s0x2l89nnhp7ex1c8 FOREIGN KEY (payer) REFERENCES public.wallet(ownerid);


--
-- Name: transfer fkmqcr388oruwt0mgr4euch226q; Type: FK CONSTRAINT; Schema: public; Owner: quarkus
--

ALTER TABLE ONLY public.transfer
    ADD CONSTRAINT fkmqcr388oruwt0mgr4euch226q FOREIGN KEY (payee) REFERENCES public.wallet(ownerid);


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
