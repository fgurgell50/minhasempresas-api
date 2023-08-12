	
CREATE DATABASE minhasempresas;

CREATE SCHEMA empresas_fornecedores;

CREATE TABLE empresas_fornecedores.empresa
(
  id serial NOT NULL,
  cnpj character varying(18) NOT NULL,
  nome_fantasia character varying(100) NOT NULL,
  cep character varying(9) NOT NULL,
  CONSTRAINT empresa_pkey PRIMARY KEY (id),
  CONSTRAINT empresa_cnpj_key UNIQUE (cnpj)
);

CREATE TABLE empresas_fornecedores.fornecedor
(
  id serial NOT NULL,
  cnpj_cpf character varying(18) NOT NULL,
  nome character varying(100) NOT NULL,
  email character varying(100),
  rg character varying(20),
  data_nascimento date,
  cep character varying(9) NOT NULL,
  CONSTRAINT fornecedor_pkey PRIMARY KEY (id),
  CONSTRAINT fornecedor_cnpj_cpf_key UNIQUE (cnpj_cpf)
);

CREATE TABLE empresas_fornecedores.empresa_fornecedor
(
  id serial NOT NULL,
  empresa_id integer,
  fornecedor_id integer,
  CONSTRAINT empresa_fornecedor_pkey PRIMARY KEY (id),
  CONSTRAINT empresa_fornecedor_empresa_id_fkey FOREIGN KEY (empresa_id)
      REFERENCES empresas_fornecedores.empresa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT empresa_fornecedor_fornecedor_id_fkey FOREIGN KEY (fornecedor_id)
      REFERENCES empresas_fornecedores.fornecedor (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);