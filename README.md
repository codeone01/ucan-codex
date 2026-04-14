# UCAN — Plataforma E-commerce de Luxo (Ficcional)

UCAN é uma plataforma fullstack de e-commerce premium com experiência realista de navegação, autenticação, verificação de e-mail, saldo ficcional, catálogo avançado, checkout com cartões falsos salvos, área do usuário e painel admin oculto.

> **Importante:** pagamentos, saldo e pedidos são ficcionais para fins de demonstração, mas a experiência foi modelada para parecer real.

---

## Sumário
1. [Visão geral](#visão-geral)
2. [Stack obrigatória utilizada](#stack-obrigatória-utilizada)
3. [Arquitetura do projeto](#arquitetura-do-projeto)
4. [Funcionalidades implementadas](#funcionalidades-implementadas)
5. [Regras de negócio principais](#regras-de-negócio-principais)
6. [Estrutura de pastas](#estrutura-de-pastas)
7. [Banco de dados (Supabase/PostgreSQL)](#banco-de-dados-supabasepostgresql)
8. [Variáveis de ambiente](#variáveis-de-ambiente)
9. [Como executar localmente](#como-executar-localmente)
10. [Fluxo funcional esperado (DoD)](#fluxo-funcional-esperado-dod)
11. [Endpoints principais da API](#endpoints-principais-da-api)
12. [Responsividade e UI premium](#responsividade-e-ui-premium)
13. [Próximos passos recomendados](#próximos-passos-recomendados)
14. [Inicialização e configuração Git/GitHub (final)](#inicialização-e-configuração-gitgithub-final)

---

## Visão geral

A UCAN foi estruturada como monorepo com:
- **Frontend** em React + jQuery (efeitos pontuais) para UI premium e responsiva.
- **Backend** em Java (Spring Boot) com autenticação JWT, regras de negócio e envio de e-mails.
- **Banco** PostgreSQL compatível com Supabase, com migrations de schema e seed.

Objetivo: entregar um fluxo completo de e-commerce de luxo fictício, incluindo:
- cadastro/login reais;
- verificação de e-mail real;
- crédito de bônus de saldo na criação e na verificação;
- filtros avançados de catálogo;
- carrinho + checkout + cartão fake reutilizável;
- pedido persistido + e-mail de confirmação;
- dashboard do usuário;
- dashboard admin oculto e protegido.

---

## Stack obrigatória utilizada

- **HTML**
- **CSS**
- **JavaScript**
- **jQuery**
- **ReactJS**
- **Java**
- **Supabase (PostgreSQL)**

---

## Arquitetura do projeto

### Frontend
- SPA React com `react-router-dom`.
- Estado global de sessão (`AuthContext`) e carrinho (`CartContext`).
- Cliente HTTP com Axios e injeção automática do token JWT.
- Guardas de rota para páginas autenticadas e admin-only.

### Backend
- Spring Boot com módulos Web, Security, JDBC, Validation e Mail.
- JWT stateless para sessão.
- Serviços separados para autenticação, catálogo, checkout, e-mail e dashboard.
- Proteção backend para rotas admin via role `ADMIN`.

### Banco
- Migrations SQL versionadas:
  - `001_schema.sql` (estrutura completa)
  - `002_seed_luxury_catalog.sql` (dados iniciais premium)

---

## Funcionalidades implementadas

### Autenticação e sessão
- Signup com nome, e-mail válido e senha.
- Login com retorno de token JWT.
- Logout removendo token local.
- Endpoint `/api/auth/me` para recuperar sessão atual.

### Verificação de e-mail
- Geração de token de verificação no cadastro.
- Envio de e-mail real via SMTP com link para frontend.
- Consumo do token na página `/verify-email`.

### Catálogo avançado
- Busca por nome do produto.
- Filtros combinados por:
  - categoria
  - subcategoria
  - marca
  - tipo
  - cor
- Ordenação por:
  - destaque
  - menor preço
  - maior preço
  - mais recentes

### Checkout e pedido
- Carrinho funcional.
- Captura de endereço de entrega.
- Cadastro de cartão fake com validação básica de formato.
- Reutilização de cartões fake salvos.
- Criação de pedido e itens.
- Dedução de saldo ficcional.
- Envio de e-mail de confirmação de compra fictícia.

### Área do usuário
- Perfil
- Saldo ficcional
- Pedidos
- Cartões fake salvos
- Endereços salvos
- Favoritos

### Admin oculto
- Rota frontend oculta (`/ops-console-7f9x`).
- Não aparece no menu público para usuários comuns.
- Proteção frontend e backend (`/api/admin/**`).
- Métricas iniciais no painel.

---

## Regras de negócio principais

1. **Ao criar conta:** creditar `R$ 100.000,00` na carteira ficcional.
2. **Ao verificar e-mail:** creditar `R$ 100.000.000,00` adicionais.
3. **Bônus de verificação apenas uma vez:**
   - logado em `email_bonus_logs`;
   - tentativa duplicada não credita novamente.
4. **Pedido confirmado:**
   - persiste pedido, itens e histórico;
   - debita saldo ficcional;
   - dispara e-mail de confirmação.

---

## Estrutura de pastas

```text
.
├── frontend/
│   ├── index.html
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── App.jsx
│       ├── main.jsx
│       ├── components/
│       │   ├── Header.jsx
│       │   ├── Footer.jsx
│       │   └── ProtectedRoute.jsx
│       ├── contexts/
│       │   ├── AuthContext.jsx
│       │   └── CartContext.jsx
│       ├── pages/
│       │   ├── HomePage.jsx
│       │   ├── auth/
│       │   ├── catalog/
│       │   ├── checkout/
│       │   ├── dashboard/
│       │   └── admin/
│       ├── services/
│       │   └── api.js
│       └── styles/
│           └── global.css
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/ucan/
│       │   ├── UcanApplication.java
│       │   ├── config/
│       │   ├── controller/
│       │   ├── security/
│       │   └── service/
│       └── resources/
│           └── application.yml
└── db/
    └── migrations/
        ├── 001_schema.sql
        └── 002_seed_luxury_catalog.sql
```

---

## Banco de dados (Supabase/PostgreSQL)

Tabelas contempladas:
- `profiles`
- `user_roles`
- `user_balances`
- `email_verification_tokens`
- `email_bonus_logs`
- `categories`
- `subcategories`
- `brands`
- `product_types`
- `colors`
- `products`
- `product_images`
- `product_videos`
- `carts`
- `cart_items`
- `fake_cards`
- `saved_addresses`
- `orders`
- `order_items`
- `order_addresses`
- `order_status_history`
- `favorites`
- `reviews`
- `banners`
- `featured_sections`
- `notifications`
- `admin_logs`

### Seed premium
A seed inclui:
- categorias de luxo obrigatórias (Nautical, Automobiles, Aerospace, Watches, etc.);
- subcategorias de exemplo;
- marcas/tipos/cores;
- produtos com dados completos fictícios (descrição curta/longa, specs, preço, estoque, badge premium, destaque, delivery, placeholders de imagem e vídeo).

---

## Variáveis de ambiente

### Backend (`backend/src/main/resources/application.yml`)
Defina no ambiente:

- `SUPABASE_DB_URL`
- `SUPABASE_DB_USER`
- `SUPABASE_DB_PASSWORD`
- `APP_JWT_SECRET` (mínimo 32 bytes)
- `FRONTEND_URL` (ex.: `http://localhost:5173`)
- `SMTP_HOST`
- `SMTP_PORT`
- `SMTP_USER`
- `SMTP_PASS`

### Frontend
- `VITE_API_URL` (padrão: `http://localhost:8080/api`)

---

## Como executar localmente

## 1) Pré-requisitos
- Node.js 18+
- npm 9+
- Java 21+
- Maven 3.9+
- PostgreSQL/Supabase disponível

## 2) Aplicar banco (schema + seed)
Execute no banco destino (Supabase SQL editor ou `psql`):

1. `db/migrations/001_schema.sql`
2. `db/migrations/002_seed_luxury_catalog.sql`

## 3) Subir backend
```bash
cd backend
mvn spring-boot:run
```
Backend em: `http://localhost:8080`

## 4) Subir frontend
```bash
cd frontend
npm install
npm run dev
```
Frontend em: `http://localhost:5173`

---

## Fluxo funcional esperado (DoD)

Checklist para validar a entrega:
- [ ] Signup funciona com e-mail válido.
- [ ] E-mail de verificação é enviado.
- [ ] Saldo inicial de `R$ 100.000,00` é creditado.
- [ ] Verificação de e-mail credita `R$ 100.000.000,00` uma única vez.
- [ ] Categorias/subcategorias carregam corretamente.
- [ ] Busca e filtros avançados funcionam em combinação.
- [ ] Carrinho funciona.
- [ ] Checkout funciona.
- [ ] Cartão fake pode ser salvo e reutilizado.
- [ ] Pedido é persistido.
- [ ] E-mail de confirmação de pedido é enviado.
- [ ] Dashboard do usuário exibe dados.
- [ ] Dashboard admin oculto funciona com proteção.
- [ ] Layout responsivo em desktop/tablet/mobile.

---

## Endpoints principais da API

### Auth
- `POST /api/auth/signup`
- `POST /api/auth/login`
- `POST /api/auth/verify-email`
- `GET /api/auth/me`

### Catálogo
- `GET /api/catalog/metadata`
- `GET /api/catalog/products?q=&category=&subcategory=&brand=&type=&color=&sort=`

### Checkout
- `GET /api/checkout/cards`
- `POST /api/checkout/orders`

### Usuário/Admin
- `GET /api/users/me/dashboard`
- `GET /api/admin/metrics` (admin)

---

## Responsividade e UI premium

- Tema escuro premium com detalhes dourados.
- Grid adaptativo para catálogo e métricas.
- Breakpoints para desktop, tablet e mobile.
- Navbar e formulários ajustados para telas menores.

---

## Próximos passos recomendados

1. Adicionar testes automatizados (unit/integration/e2e).
2. Implementar paginação e cache de catálogo.
3. Fortalecer validações backend (DTOs tipados para checkout e catálogo).
4. Criptografar/mask de cartões fake em repouso (mesmo sendo fictícios).
5. Adicionar auditoria admin detalhada (`admin_logs`) em todas operações CRUD.
6. Implementar upload real de mídia via storage (Supabase Storage).
7. Adicionar observabilidade (logs estruturados, tracing, métricas).

---

## Inicialização e configuração Git/GitHub (final)

### 1) Inicializar Git (caso projeto novo)
```bash
git init
git add .
git commit -m "chore: bootstrap UCAN"
```

### 2) Criar repositório no GitHub
- Crie um repositório (ex.: `ucan` ou `ucan-codex`) na sua conta/organização.
- **Não** inicialize com README remoto se já existe local.

### 3) Conectar remoto e enviar
```bash
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/SEU_REPO.git
git push -u origin main
```

### 4) Fluxo recomendado de branches
```bash
git checkout -b feat/nome-da-feature
# ... alterações
git add .
git commit -m "feat: descrição objetiva"
git push -u origin feat/nome-da-feature
```
Abra PR para `main` no GitHub.

### 5) Convenção de commits sugerida
- `feat:` nova funcionalidade
- `fix:` correção
- `refactor:` refatoração sem mudança funcional
- `docs:` documentação
- `chore:` tarefas de manutenção
- `test:` testes

### 6) Configuração de proteção no GitHub (recomendado)
No branch `main`:
- exigir PR para merge;
- exigir checks de CI;
- bloquear push direto;
- exigir ao menos 1 aprovação de review.

### 7) Exemplo de CI mínimo (sugestão)
- Job frontend: install + build
- Job backend: build + tests
- Job db: validação de migrations

---

Se quiser, no próximo passo eu posso gerar também:
1. `.env.example` para frontend e backend;
2. workflow GitHub Actions completo;
3. script único `make dev` para subir tudo rapidamente.
