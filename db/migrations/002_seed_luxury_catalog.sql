insert into categories(name, slug) values
('Nautical','nautical'),('Automobiles','automobiles'),('Aerospace','aerospace'),('Watches','watches'),('Arts','arts'),('Jewelry','jewelry'),('Fashion','fashion'),('Real Estate','real-estate'),('Furniture','furniture'),('Electronics','electronics'),('Perfumery','perfumery'),('Travel and Experiences','travel-experiences'),('Luxury Decor','luxury-decor'),('Premium Writing and Office','premium-writing-office'),('Collectibles','collectibles')
on conflict do nothing;

insert into subcategories(category_id,name,slug)
select c.id, v.name, v.slug from categories c join (values
('nautical','Boats','boats'),('nautical','Yachts','yachts'),('nautical','Jet Skis','jet-skis'),('nautical','Speedboats','speedboats'),('nautical','Catamarans','catamarans'),
('automobiles','Cars','cars'),('automobiles','Motorcycles','motorcycles'),('automobiles','Luxury SUVs','luxury-suvs'),('automobiles','Sports Cars','sports-cars'),('automobiles','Collectible Classics','collectible-classics'),
('aerospace','Executive Jets','executive-jets'),('aerospace','Helicopters','helicopters'),('aerospace','Light Aircraft','light-aircraft'),
('watches','Swiss Watches','swiss-watches'),('watches','Luxury Sports Watches','luxury-sports-watches'),('watches','Classic Watches','classic-watches'),('watches','Limited Editions','limited-editions'),('watches','Premium Smartwatches','premium-smartwatches'),
('jewelry','Rings','rings'),('jewelry','Necklaces','necklaces'),('fashion','Suits','suits'),('real-estate','Mansions','mansions'),('furniture','Premium Sofas','premium-sofas'),('electronics','Luxury Smartphones','luxury-smartphones'),('travel-experiences','Private Expeditions','private-expeditions')
) v(cat_slug,name,slug) on c.slug=v.cat_slug
on conflict do nothing;

insert into brands(name, slug) values
('Aurum Marine','aurum-marine'),('Velocità Royale','velocita-royale'),('Altissima Air','altissima-air'),('Chronos Élite','chronos-elite'),('Maison Ornée','maison-ornee'),('Imperium Estates','imperium-estates'),('Noir Atelier','noir-atelier'),('Celestia Labs','celestia-labs')
on conflict do nothing;

insert into product_types(name,slug) values
('Limited','limited'),('Signature','signature'),('Collector','collector'),('Bespoke','bespoke'),('Performance','performance')
on conflict do nothing;

insert into colors(name,slug) values
('Black','black'),('White','white'),('Silver','silver'),('Gold','gold'),('Blue','blue'),('Red','red'),('Navy','navy'),('Ivory','ivory')
on conflict do nothing;

with refs as (
  select s.id sub_id, c.id cat_id from subcategories s join categories c on c.id=s.category_id
), b as (select * from brands), t as (select * from product_types), co as (select * from colors)
insert into products(category_id, subcategory_id, brand_id, type_id, color_id, name, short_description, long_description, specifications, price, stock, premium_badge, featured, fictional_delivery_time)
select cat_id, sub_id,
  (select id from b order by random() limit 1),
  (select id from t order by random() limit 1),
  (select id from co order by random() limit 1),
  initcap(replace((select slug from subcategories where id=sub_id),'-',' ')) || ' Prestige ' || row_number() over (),
  'Ultra-luxury curated item for elite customers.',
  'Handcrafted fictional product with concierge support, insured logistics and white-glove delivery.',
  jsonb_build_object('warranty','5 years','origin','Fictional Atelier','edition','Limited'),
  (100000 + (row_number() over () * 85000))::numeric(18,2),
  3 + (row_number() over () % 7),
  true,
  (row_number() over () % 3 = 0),
  '7-21 business days'
from refs
on conflict do nothing;

insert into product_images(product_id, image_url, is_primary)
select id, 'https://placehold.co/1200x800?text=UCAN+' || id, true from products
on conflict do nothing;

insert into product_videos(product_id, video_url)
select id, 'https://example.com/video/' || id from products
on conflict do nothing;
