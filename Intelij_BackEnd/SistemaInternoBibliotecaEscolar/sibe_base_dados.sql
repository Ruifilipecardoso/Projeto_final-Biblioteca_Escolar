--
-- PostgreSQL database dump
--

\restrict l96QH6tYf6mY9bKp8JRvzmOOO2PYgVug35mxwNyEBpBVeg8YmAfdX81RhuqfgbO

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-06-02 14:35:30

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
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
-- TOC entry 222 (class 1259 OID 24590)
-- Name: aluno; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.aluno (
    id_aluno integer NOT NULL,
    nome character varying(55) NOT NULL,
    contacto character varying(15) NOT NULL,
    nif character varying(9) NOT NULL,
    status character varying(25) NOT NULL,
    numero_escolar character varying(20) NOT NULL,
    id_utilizador integer
);


ALTER TABLE public.aluno OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 24589)
-- Name: aluno_id_aluno_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.aluno_id_aluno_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.aluno_id_aluno_seq OWNER TO postgres;

--
-- TOC entry 5107 (class 0 OID 0)
-- Dependencies: 221
-- Name: aluno_id_aluno_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.aluno_id_aluno_seq OWNED BY public.aluno.id_aluno;


--
-- TOC entry 220 (class 1259 OID 24578)
-- Name: bibliotecario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bibliotecario (
    id_bibliotecario integer NOT NULL,
    nome character varying(55) NOT NULL,
    numero_escolar character varying(20) NOT NULL,
    id_utilizador integer
);


ALTER TABLE public.bibliotecario OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 24577)
-- Name: bibliotecario_id_bibliotecario_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bibliotecario_id_bibliotecario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bibliotecario_id_bibliotecario_seq OWNER TO postgres;

--
-- TOC entry 5108 (class 0 OID 0)
-- Dependencies: 219
-- Name: bibliotecario_id_bibliotecario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bibliotecario_id_bibliotecario_seq OWNED BY public.bibliotecario.id_bibliotecario;


--
-- TOC entry 229 (class 1259 OID 24641)
-- Name: devolucao_ideal; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.devolucao_ideal (
    id_devolucao_i integer NOT NULL,
    data date NOT NULL,
    hora time without time zone NOT NULL,
    estado_devolucao character varying(25) NOT NULL,
    id_emprestimo integer NOT NULL
);


ALTER TABLE public.devolucao_ideal OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 24640)
-- Name: devolucao_ideal_id_devolucao_i_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.devolucao_ideal_id_devolucao_i_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.devolucao_ideal_id_devolucao_i_seq OWNER TO postgres;

--
-- TOC entry 5109 (class 0 OID 0)
-- Dependencies: 228
-- Name: devolucao_ideal_id_devolucao_i_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.devolucao_ideal_id_devolucao_i_seq OWNED BY public.devolucao_ideal.id_devolucao_i;


--
-- TOC entry 231 (class 1259 OID 24655)
-- Name: devolucao_real; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.devolucao_real (
    id_devolucao_r integer NOT NULL,
    data date,
    hora time without time zone,
    qualidade character varying(55),
    id_emprestimo integer NOT NULL
);


ALTER TABLE public.devolucao_real OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 24654)
-- Name: devolucao_real_id_devolucao_r_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.devolucao_real_id_devolucao_r_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.devolucao_real_id_devolucao_r_seq OWNER TO postgres;

--
-- TOC entry 5110 (class 0 OID 0)
-- Dependencies: 230
-- Name: devolucao_real_id_devolucao_r_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.devolucao_real_id_devolucao_r_seq OWNED BY public.devolucao_real.id_devolucao_r;


--
-- TOC entry 224 (class 1259 OID 24604)
-- Name: emprestimo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.emprestimo (
    id_emprestimo integer NOT NULL,
    data date NOT NULL,
    hora time without time zone NOT NULL,
    estado_emprestimo character varying(25) NOT NULL,
    id_bibliotecario integer NOT NULL,
    id_aluno integer NOT NULL
);


ALTER TABLE public.emprestimo OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 24603)
-- Name: emprestimo_id_emprestimo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.emprestimo_id_emprestimo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.emprestimo_id_emprestimo_seq OWNER TO postgres;

--
-- TOC entry 5111 (class 0 OID 0)
-- Dependencies: 223
-- Name: emprestimo_id_emprestimo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.emprestimo_id_emprestimo_seq OWNED BY public.emprestimo.id_emprestimo;


--
-- TOC entry 227 (class 1259 OID 24632)
-- Name: linha_livros; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.linha_livros (
    id_livro integer NOT NULL,
    id_emprestimo integer NOT NULL,
    qualidade character varying(55) NOT NULL
);


ALTER TABLE public.linha_livros OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 24617)
-- Name: livro; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.livro (
    id_livro integer NOT NULL,
    titulo character varying(255) NOT NULL,
    categoria character varying(100) NOT NULL,
    autor character varying(55) NOT NULL,
    disponibilidade character varying(25) NOT NULL,
    stock_atual integer CONSTRAINT livro_stock_not_null NOT NULL,
    isbn character varying(13) NOT NULL,
    stock_total integer NOT NULL,
    editora character varying(100),
    num_paginas integer,
    ano_publicacao integer,
    capa_url character varying(500)
);


ALTER TABLE public.livro OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 24616)
-- Name: livro_id_livro_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.livro_id_livro_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.livro_id_livro_seq OWNER TO postgres;

--
-- TOC entry 5112 (class 0 OID 0)
-- Dependencies: 225
-- Name: livro_id_livro_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.livro_id_livro_seq OWNED BY public.livro.id_livro;


--
-- TOC entry 233 (class 1259 OID 24702)
-- Name: utilizador; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utilizador (
    id_utilizador integer NOT NULL,
    email character varying(55) NOT NULL,
    senha character varying(255) NOT NULL,
    perfil character varying(25) NOT NULL,
    imagem_perfil character varying(500)
);


ALTER TABLE public.utilizador OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 24701)
-- Name: utilizador_id_utilizador_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.utilizador_id_utilizador_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utilizador_id_utilizador_seq OWNER TO postgres;

--
-- TOC entry 5113 (class 0 OID 0)
-- Dependencies: 232
-- Name: utilizador_id_utilizador_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.utilizador_id_utilizador_seq OWNED BY public.utilizador.id_utilizador;


--
-- TOC entry 4891 (class 2604 OID 24593)
-- Name: aluno id_aluno; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aluno ALTER COLUMN id_aluno SET DEFAULT nextval('public.aluno_id_aluno_seq'::regclass);


--
-- TOC entry 4890 (class 2604 OID 24581)
-- Name: bibliotecario id_bibliotecario; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bibliotecario ALTER COLUMN id_bibliotecario SET DEFAULT nextval('public.bibliotecario_id_bibliotecario_seq'::regclass);


--
-- TOC entry 4894 (class 2604 OID 24644)
-- Name: devolucao_ideal id_devolucao_i; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devolucao_ideal ALTER COLUMN id_devolucao_i SET DEFAULT nextval('public.devolucao_ideal_id_devolucao_i_seq'::regclass);


--
-- TOC entry 4895 (class 2604 OID 24658)
-- Name: devolucao_real id_devolucao_r; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devolucao_real ALTER COLUMN id_devolucao_r SET DEFAULT nextval('public.devolucao_real_id_devolucao_r_seq'::regclass);


--
-- TOC entry 4892 (class 2604 OID 24607)
-- Name: emprestimo id_emprestimo; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emprestimo ALTER COLUMN id_emprestimo SET DEFAULT nextval('public.emprestimo_id_emprestimo_seq'::regclass);


--
-- TOC entry 4893 (class 2604 OID 24620)
-- Name: livro id_livro; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.livro ALTER COLUMN id_livro SET DEFAULT nextval('public.livro_id_livro_seq'::regclass);


--
-- TOC entry 4896 (class 2604 OID 24705)
-- Name: utilizador id_utilizador; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilizador ALTER COLUMN id_utilizador SET DEFAULT nextval('public.utilizador_id_utilizador_seq'::regclass);


--
-- TOC entry 5090 (class 0 OID 24590)
-- Dependencies: 222
-- Data for Name: aluno; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.aluno (id_aluno, nome, contacto, nif, status, numero_escolar, id_utilizador) FROM stdin;
1	Maria Soares	912345678	234567890	Bom	A2026_001	2
\.


--
-- TOC entry 5088 (class 0 OID 24578)
-- Dependencies: 220
-- Data for Name: bibliotecario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bibliotecario (id_bibliotecario, nome, numero_escolar, id_utilizador) FROM stdin;
1	Rúben Ferreira	B2026_01	3
\.


--
-- TOC entry 5097 (class 0 OID 24641)
-- Dependencies: 229
-- Data for Name: devolucao_ideal; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.devolucao_ideal (id_devolucao_i, data, hora, estado_devolucao, id_emprestimo) FROM stdin;
2	2026-06-12	23:59:59	Agendado	5
3	2026-06-12	23:59:59	Ativo	6
4	2026-06-12	23:59:59	Devolvido	7
\.


--
-- TOC entry 5099 (class 0 OID 24655)
-- Dependencies: 231
-- Data for Name: devolucao_real; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.devolucao_real (id_devolucao_r, data, hora, qualidade, id_emprestimo) FROM stdin;
1	2026-05-30	14:30:00	Impecável	7
\.


--
-- TOC entry 5092 (class 0 OID 24604)
-- Dependencies: 224
-- Data for Name: emprestimo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.emprestimo (id_emprestimo, data, hora, estado_emprestimo, id_bibliotecario, id_aluno) FROM stdin;
4	2026-05-29	16:52:19.812699	Solicitado	1	1
5	2026-05-29	16:52:19.812699	Agendado	1	1
6	2026-05-29	16:52:19.812699	Ativo	1	1
7	2026-05-29	16:52:19.812699	Devolvido	1	1
8	2026-06-01	10:15:00	Solicitado	1	1
9	2026-06-01	11:30:00	Solicitado	1	1
10	2026-06-01	14:20:00	Solicitado	1	1
11	2026-06-01	15:45:00	Solicitado	1	1
\.


--
-- TOC entry 5095 (class 0 OID 24632)
-- Dependencies: 227
-- Data for Name: linha_livros; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.linha_livros (id_livro, id_emprestimo, qualidade) FROM stdin;
1	4	Impecável
4	5	Impecável
4	6	Impecável
2	5	Impecável
3	6	Impecável
4	7	Impecável
1	8	Novo
4	8	Novo
5	8	Novo
6	8	Novo
2	9	Novo
8	9	Novo
3	10	Novo
9	10	Novo
10	10	Novo
7	11	Novo
\.


--
-- TOC entry 5094 (class 0 OID 24617)
-- Dependencies: 226
-- Data for Name: livro; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.livro (id_livro, titulo, categoria, autor, disponibilidade, stock_atual, isbn, stock_total, editora, num_paginas, ano_publicacao, capa_url) FROM stdin;
1	O Principezinho	Infantil	Antoine de Saint-Exupéry	Disponível	5	9789722210515	5	Editorial Aster	96	1943	imagens/capas/principezinho.jpg
2	Memorial do Convento	Romance	José Saramago	Disponível	3	9789722100175	3	Caminho	360	1982	imagens/capas/memorial.jpg
3	Os Maias	Clássico	Eça de Queirós	Disponível	4	9789720049445	4	Porto Editora	736	1888	imagens/capas/maias.jpg
4	Mensagem	Poesia	Fernando Pessoa	Disponível	6	9789720049476	6	Porto Editora	120	1934	imagens/capas/mensagem.jpg
5	A Lua de Joana	Drama	Maria Teresa Maia Gonzalez	Disponível	2	9789722320474	2	Editorial Presença	176	1994	imagens/capas/lua_joana.jpg
6	O Alquimista	Ficção	Paulo Coelho	Disponível	5	9789727110056	5	Pergaminho	192	1988	imagens/capas/alquimista.jpg
7	1984	Distopia	George Orwell	Disponível	4	9789722340359	4	Editorial Presença	328	1949	imagens/capas/1984.jpg
8	Ensaio sobre a Cegueira	Ficção	José Saramago	Disponível	3	9789722110204	3	Caminho	312	1995	imagens/capas/cegueira.jpg
9	Amor de Perdição	Romance	Camilo Castelo Branco	Disponível	2	9789720049322	2	Porto Editora	288	1862	imagens/capas/amor_perdicao.jpg
10	O Hobbit	Fantasia	J.R.R. Tolkien	Disponível	4	9789896579548	4	Planeta Manuscrito	320	1937	imagens/capas/hobbit.jpg
\.


--
-- TOC entry 5101 (class 0 OID 24702)
-- Dependencies: 233
-- Data for Name: utilizador; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utilizador (id_utilizador, email, senha, perfil, imagem_perfil) FROM stdin;
1	admin@biblioteca.pt	$2a$10$h6ViOxnbWg.Y5/M21k9opOYFKHuQGGYN39pMaVMEdDJJfeW1k3zwq	Admin	adminRui.png
2	alunoMaria@biblioteca.pt	$2a$10$y6XYZG8LaMceQbrwLgdU0uPwftCMerfgrmYscyOVaamyP.8baj2li	Aluno	alunoMaria.png
3	bibliotecarioRuben@biblioteca.pt	$2a$10$5rfs1p1AMojUaO6/gBz8RuB73.BYDX6XQoC47VmsvR0NjuvpqLBuS	Bibliotecario	bibliotecarioRuben.png
\.


--
-- TOC entry 5114 (class 0 OID 0)
-- Dependencies: 221
-- Name: aluno_id_aluno_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.aluno_id_aluno_seq', 1, true);


--
-- TOC entry 5115 (class 0 OID 0)
-- Dependencies: 219
-- Name: bibliotecario_id_bibliotecario_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bibliotecario_id_bibliotecario_seq', 1, true);


--
-- TOC entry 5116 (class 0 OID 0)
-- Dependencies: 228
-- Name: devolucao_ideal_id_devolucao_i_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.devolucao_ideal_id_devolucao_i_seq', 4, true);


--
-- TOC entry 5117 (class 0 OID 0)
-- Dependencies: 230
-- Name: devolucao_real_id_devolucao_r_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.devolucao_real_id_devolucao_r_seq', 1, true);


--
-- TOC entry 5118 (class 0 OID 0)
-- Dependencies: 223
-- Name: emprestimo_id_emprestimo_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.emprestimo_id_emprestimo_seq', 11, true);


--
-- TOC entry 5119 (class 0 OID 0)
-- Dependencies: 225
-- Name: livro_id_livro_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.livro_id_livro_seq', 10, true);


--
-- TOC entry 5120 (class 0 OID 0)
-- Dependencies: 232
-- Name: utilizador_id_utilizador_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.utilizador_id_utilizador_seq', 3, true);


--
-- TOC entry 4904 (class 2606 OID 24727)
-- Name: aluno aluno_id_utilizador_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aluno
    ADD CONSTRAINT aluno_id_utilizador_key UNIQUE (id_utilizador);


--
-- TOC entry 4906 (class 2606 OID 24602)
-- Name: aluno aluno_nif_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aluno
    ADD CONSTRAINT aluno_nif_key UNIQUE (nif);


--
-- TOC entry 4908 (class 2606 OID 24700)
-- Name: aluno aluno_numero_escolar_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aluno
    ADD CONSTRAINT aluno_numero_escolar_key UNIQUE (numero_escolar);


--
-- TOC entry 4910 (class 2606 OID 24600)
-- Name: aluno aluno_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aluno
    ADD CONSTRAINT aluno_pkey PRIMARY KEY (id_aluno);


--
-- TOC entry 4898 (class 2606 OID 24734)
-- Name: bibliotecario bibliotecario_id_utilizador_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bibliotecario
    ADD CONSTRAINT bibliotecario_id_utilizador_key UNIQUE (id_utilizador);


--
-- TOC entry 4900 (class 2606 OID 24697)
-- Name: bibliotecario bibliotecario_numero_escolar_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bibliotecario
    ADD CONSTRAINT bibliotecario_numero_escolar_key UNIQUE (numero_escolar);


--
-- TOC entry 4902 (class 2606 OID 24586)
-- Name: bibliotecario bibliotecario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bibliotecario
    ADD CONSTRAINT bibliotecario_pkey PRIMARY KEY (id_bibliotecario);


--
-- TOC entry 4920 (class 2606 OID 24653)
-- Name: devolucao_ideal devolucao_ideal_id_emprestimo_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devolucao_ideal
    ADD CONSTRAINT devolucao_ideal_id_emprestimo_key UNIQUE (id_emprestimo);


--
-- TOC entry 4922 (class 2606 OID 24651)
-- Name: devolucao_ideal devolucao_ideal_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devolucao_ideal
    ADD CONSTRAINT devolucao_ideal_pkey PRIMARY KEY (id_devolucao_i);


--
-- TOC entry 4924 (class 2606 OID 24664)
-- Name: devolucao_real devolucao_real_id_emprestimo_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devolucao_real
    ADD CONSTRAINT devolucao_real_id_emprestimo_key UNIQUE (id_emprestimo);


--
-- TOC entry 4926 (class 2606 OID 24662)
-- Name: devolucao_real devolucao_real_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devolucao_real
    ADD CONSTRAINT devolucao_real_pkey PRIMARY KEY (id_devolucao_r);


--
-- TOC entry 4912 (class 2606 OID 24615)
-- Name: emprestimo emprestimo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emprestimo
    ADD CONSTRAINT emprestimo_pkey PRIMARY KEY (id_emprestimo);


--
-- TOC entry 4918 (class 2606 OID 24639)
-- Name: linha_livros linha_livros_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.linha_livros
    ADD CONSTRAINT linha_livros_pkey PRIMARY KEY (id_livro, id_emprestimo);


--
-- TOC entry 4914 (class 2606 OID 24631)
-- Name: livro livro_isbn_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.livro
    ADD CONSTRAINT livro_isbn_key UNIQUE (isbn);


--
-- TOC entry 4916 (class 2606 OID 24629)
-- Name: livro livro_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.livro
    ADD CONSTRAINT livro_pkey PRIMARY KEY (id_livro);


--
-- TOC entry 4928 (class 2606 OID 24713)
-- Name: utilizador utilizador_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilizador
    ADD CONSTRAINT utilizador_email_key UNIQUE (email);


--
-- TOC entry 4930 (class 2606 OID 24711)
-- Name: utilizador utilizador_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilizador
    ADD CONSTRAINT utilizador_pkey PRIMARY KEY (id_utilizador);


--
-- TOC entry 4938 (class 2606 OID 24675)
-- Name: devolucao_ideal devolucao_ideal_id_emprestimo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devolucao_ideal
    ADD CONSTRAINT devolucao_ideal_id_emprestimo_fkey FOREIGN KEY (id_emprestimo) REFERENCES public.emprestimo(id_emprestimo) ON DELETE CASCADE;


--
-- TOC entry 4939 (class 2606 OID 24680)
-- Name: devolucao_real devolucao_real_id_emprestimo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devolucao_real
    ADD CONSTRAINT devolucao_real_id_emprestimo_fkey FOREIGN KEY (id_emprestimo) REFERENCES public.emprestimo(id_emprestimo) ON DELETE CASCADE;


--
-- TOC entry 4934 (class 2606 OID 24665)
-- Name: emprestimo emprestimo_id_aluno_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emprestimo
    ADD CONSTRAINT emprestimo_id_aluno_fkey FOREIGN KEY (id_aluno) REFERENCES public.aluno(id_aluno) ON DELETE RESTRICT;


--
-- TOC entry 4935 (class 2606 OID 24670)
-- Name: emprestimo emprestimo_id_bibliotecario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emprestimo
    ADD CONSTRAINT emprestimo_id_bibliotecario_fkey FOREIGN KEY (id_bibliotecario) REFERENCES public.bibliotecario(id_bibliotecario) ON DELETE RESTRICT;


--
-- TOC entry 4933 (class 2606 OID 24728)
-- Name: aluno fk_aluno_utilizador; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aluno
    ADD CONSTRAINT fk_aluno_utilizador FOREIGN KEY (id_utilizador) REFERENCES public.utilizador(id_utilizador) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4931 (class 2606 OID 24735)
-- Name: bibliotecario fk_biblio_utilizador; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bibliotecario
    ADD CONSTRAINT fk_biblio_utilizador FOREIGN KEY (id_utilizador) REFERENCES public.utilizador(id_utilizador) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4932 (class 2606 OID 24741)
-- Name: bibliotecario fk_bibliotecario_utilizador; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bibliotecario
    ADD CONSTRAINT fk_bibliotecario_utilizador FOREIGN KEY (id_utilizador) REFERENCES public.utilizador(id_utilizador);


--
-- TOC entry 4936 (class 2606 OID 24690)
-- Name: linha_livros linha_livros_id_emprestimo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.linha_livros
    ADD CONSTRAINT linha_livros_id_emprestimo_fkey FOREIGN KEY (id_emprestimo) REFERENCES public.emprestimo(id_emprestimo) ON DELETE CASCADE;


--
-- TOC entry 4937 (class 2606 OID 24685)
-- Name: linha_livros linha_livros_id_livro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.linha_livros
    ADD CONSTRAINT linha_livros_id_livro_fkey FOREIGN KEY (id_livro) REFERENCES public.livro(id_livro) ON DELETE RESTRICT;


-- Completed on 2026-06-02 14:35:31

--
-- PostgreSQL database dump complete
--

\unrestrict l96QH6tYf6mY9bKp8JRvzmOOO2PYgVug35mxwNyEBpBVeg8YmAfdX81RhuqfgbO

