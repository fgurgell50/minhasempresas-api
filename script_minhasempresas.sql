-- Database: minhasempresas

-- DROP DATABASE minhasempresas;

CREATE DATABASE minhasempresas
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Portuguese_Brazil.1252'
       LC_CTYPE = 'Portuguese_Brazil.1252'
       CONNECTION LIMIT = -1;

CREATE SCHEMA empresas_fornecedores
  AUTHORIZATION postgres;


CREATE TABLE empresas_fornecedores.empresa
(
  empresa_id bigint NOT NULL DEFAULT nextval('empresas_fornecedores.empresa_id_seq'::regclass),
  cnpj character varying(255) NOT NULL,
  nome_fantasia character varying(255) NOT NULL,
  cep character varying(255) NOT NULL,
  empresa_fornecedor_id integer,
  id bigint NOT NULL DEFAULT nextval('empresas_fornecedores.empresa_id_seq1'::regclass),
  CONSTRAINT empresa_pkey PRIMARY KEY (empresa_id),
  CONSTRAINT empresa_cnpj_key UNIQUE (cnpj)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE empresas_fornecedores.empresa
  OWNER TO postgres;


CREATE TABLE empresas_fornecedores.empresa_fornecedor
(
  empresa_id integer NOT NULL,
  fornecedor_id integer NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE empresas_fornecedores.empresa_fornecedor
  OWNER TO postgres;


CREATE TABLE empresas_fornecedores.fornecedor
(
  fornecedor_id bigint NOT NULL DEFAULT nextval('empresas_fornecedores.fornecedor_id_seq'::regclass),
  cnpj_cpf character varying(255) NOT NULL,
  nome character varying(255) NOT NULL,
  email character varying(255),
  rg character varying(255),
  data_nascimento date,
  cep character varying(255) NOT NULL,
  empresa_fornecedor_id integer,
  CONSTRAINT fornecedor_pkey PRIMARY KEY (fornecedor_id),
  CONSTRAINT empresa_cnpj_cpf_key UNIQUE (cnpj_cpf)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE empresas_fornecedores.fornecedor
  OWNER TO postgres;


