create extension if not exists "pgcrypto";

create table if not exists profiles (
  id uuid primary key default gen_random_uuid(),
  full_name text not null,
  email text unique not null,
  password_hash text not null,
  email_verified boolean not null default false,
  created_at timestamptz default now()
);
create table if not exists user_roles (
  user_id uuid references profiles(id) on delete cascade,
  role text not null check (role in ('customer', 'admin')),
  primary key(user_id)
);
create table if not exists user_balances (
  user_id uuid primary key references profiles(id) on delete cascade,
  balance numeric(18,2) not null default 0
);
create table if not exists email_verification_tokens (
  id bigserial primary key,
  user_id uuid references profiles(id) on delete cascade,
  token text unique not null,
  expires_at timestamptz not null,
  used boolean default false
);
create table if not exists email_bonus_logs (
  id bigserial primary key,
  user_id uuid references profiles(id) on delete cascade,
  bonus_type text not null,
  amount numeric(18,2) not null,
  created_at timestamptz default now()
);
create table if not exists categories (id bigserial primary key, name text not null, slug text unique not null);
create table if not exists subcategories (id bigserial primary key, category_id bigint references categories(id), name text not null, slug text unique not null);
create table if not exists brands (id bigserial primary key, name text not null, slug text unique not null);
create table if not exists product_types (id bigserial primary key, name text not null, slug text unique not null);
create table if not exists colors (id bigserial primary key, name text not null, slug text unique not null);
create table if not exists products (
  id bigserial primary key,
  category_id bigint references categories(id),
  subcategory_id bigint references subcategories(id),
  brand_id bigint references brands(id),
  type_id bigint references product_types(id),
  color_id bigint references colors(id),
  name text not null,
  short_description text,
  long_description text,
  specifications jsonb,
  price numeric(18,2) not null,
  stock int not null,
  premium_badge boolean default true,
  featured boolean default false,
  fictional_delivery_time text,
  created_at timestamptz default now()
);
create table if not exists product_images (id bigserial primary key, product_id bigint references products(id) on delete cascade, image_url text not null, is_primary boolean default false);
create table if not exists product_videos (id bigserial primary key, product_id bigint references products(id) on delete cascade, video_url text not null);
create table if not exists carts (id bigserial primary key, user_id uuid references profiles(id) on delete cascade, created_at timestamptz default now());
create table if not exists cart_items (id bigserial primary key, cart_id bigint references carts(id) on delete cascade, product_id bigint references products(id), quantity int not null);
create table if not exists fake_cards (
  id bigserial primary key,
  user_id uuid references profiles(id) on delete cascade,
  printed_name text not null,
  card_number text not null,
  expiration text not null,
  cvv text not null,
  nickname text not null,
  brand text not null,
  created_at timestamptz default now()
);
create table if not exists saved_addresses (
  id bigserial primary key,
  user_id uuid references profiles(id) on delete cascade,
  address_line text not null,
  city text not null,
  state text not null,
  zip_code text not null,
  country text not null,
  created_at timestamptz default now()
);
create table if not exists orders (
  id uuid primary key,
  user_id uuid references profiles(id),
  order_code text unique not null,
  status text not null,
  total_amount numeric(18,2) not null,
  payment_method text not null,
  created_at timestamptz default now()
);
create table if not exists order_items (id bigserial primary key, order_id uuid references orders(id) on delete cascade, product_id bigint references products(id), quantity int not null, unit_price numeric(18,2) not null);
create table if not exists order_addresses (id bigserial primary key, order_id uuid references orders(id) on delete cascade, address_line text, city text, state text, zip_code text, country text);
create table if not exists order_status_history (id bigserial primary key, order_id uuid references orders(id) on delete cascade, status text not null, note text, created_at timestamptz default now());
create table if not exists favorites (id bigserial primary key, user_id uuid references profiles(id) on delete cascade, product_id bigint references products(id), unique(user_id, product_id));
create table if not exists reviews (id bigserial primary key, user_id uuid references profiles(id), product_id bigint references products(id), rating int check(rating between 1 and 5), comment text, created_at timestamptz default now());
create table if not exists banners (id bigserial primary key, title text, image_url text, active boolean default true);
create table if not exists featured_sections (id bigserial primary key, title text, slug text unique);
create table if not exists notifications (id bigserial primary key, user_id uuid references profiles(id), title text, body text, read boolean default false);
create table if not exists admin_logs (id bigserial primary key, admin_user_id uuid references profiles(id), action text, metadata jsonb, created_at timestamptz default now());
