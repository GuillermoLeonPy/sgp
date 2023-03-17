/*77-pms_production_activity_instance-HISTORIC-RECORDS-201611071308*/



create table pms_production_activity_instance_history(
id	   		 		   				bigint,
id_production_activity 		   			bigint NOT NULL,
id_person		 						bigint,--a functionary person
id_order							bigint NOT NULL,
id_order_item							bigint NOT NULL,
id_product							bigint NOT NULL,
require_parcial_product_recall			varchar(1) NOT NULL default 'S',
parcial_product_recall_date					timestamp,
recall_locker_number				bigint,
--only the first production process activity instance does not require partial product recall
is_asignable	 		   				varchar(1) NOT NULL default 'N',
--the (activity_instance_unique_number - 1) must be in partial result delivered status
assignment_date						timestamp,
--the id_person functionary attribute has been setted
delivers_product_instance   				varchar(1) NOT NULL default 'N',
--majority of instantiated activities do not deliver product instance
delivers_partial_result   				varchar(1) NOT NULL default 'S',
partial_result_delivery_date   			timestamp,
occupied_locker_number				bigint,
--majority of instantiated activities deliver partial result
status								varchar(50) not null default 'application.common.status.pending',
previous_status						varchar(50),
--convinient attribute because not all the activities instances pass through the same stages or status
next_status							varchar(50) default 'application.common.status.assigned',
--convinient attribute because not all the activities instances pass through the same stages or status
activity_instance_unique_number			bigint NOT NULL,
product_instance_unique_number			bigint NOT NULL,
instantiation_date						timestamp not null default now(),
activity_start_work_date					timestamp,
--the date when the activity reach the initiated status
activity_finish_work_date				timestamp,
--the date when the activity reach the finalized status
activity_cancellation_date				timestamp,
activity_cancellation_reason_description 	varchar(800),
creation_user	 		   				varchar(50),
creation_date			   				timestamp NOT NULL default now(),
last_modif_user 		   				varchar(50),
last_modif_date		   				timestamp
);

--ALTER TABLE pms_production_activity_instance_history ADD id_order bigint NOT NULL;
--ALTER TABLE pms_production_activity_instance_history ADD parcial_product_recall_date timestamp;
--ALTER TABLE pms_production_activity_instance_history ADD recall_locker_number bigint;
--ALTER TABLE pms_production_activity_instance_history ADD partial_result_delivery_date timestamp;
--ALTER TABLE pms_production_activity_instance_history ADD occupied_locker_number bigint;

ALTER TABLE pms_production_activity_instance_history 
ADD constraint chk_require_parcial_product_recall 
CHECK (require_parcial_product_recall in ('S', 'N'));

ALTER TABLE pms_production_activity_instance_history 
ADD constraint chk_is_asignable
CHECK (is_asignable in ('S', 'N'));

ALTER TABLE pms_production_activity_instance_history 
ADD constraint chk_delivers_product_instance
CHECK (delivers_product_instance in ('S', 'N'));

ALTER TABLE pms_production_activity_instance_history 
ADD constraint chk_delivers_partial_result
CHECK (delivers_partial_result in ('S', 'N'));

ALTER TABLE pms_production_activity_instance_history ADD CONSTRAINT 
chk_activity_finish_work_date CHECK (activity_finish_work_date > activity_start_work_date);

ALTER TABLE pms_production_activity_instance_history ADD CONSTRAINT 
chk_activity_start_work_date CHECK (activity_start_work_date > instantiation_date);

ALTER TABLE pms_production_activity_instance_history ADD CONSTRAINT 
chk_activity_cancellation_date CHECK (activity_cancellation_date > instantiation_date);

ALTER TABLE pms_production_activity_instance_history 
ADD CONSTRAINT pms_production_activity_instance_history_id_pk
PRIMARY KEY (id);

ALTER TABLE pms_production_activity_instance_history 
ADD constraint pms_production_activity_instance_history_fk_01
FOREIGN KEY (id_production_activity) 
REFERENCES pms_production_process_activity (id);

ALTER TABLE pms_production_activity_instance_history 
ADD constraint pms_production_activity_instance_history_fk_02
FOREIGN KEY (id_production_activity) 
REFERENCES pms_production_process_activity (id);

ALTER TABLE pms_production_activity_instance_history 
ADD constraint pms_production_activity_instance_history_fk_03
FOREIGN KEY (id_person) 
REFERENCES pms_person (id);

ALTER TABLE pms_production_activity_instance_history 
ADD constraint pms_production_activity_instance_history_fk_04
FOREIGN KEY (id_order_item) 
REFERENCES pms_order_item (id);

ALTER TABLE pms_production_activity_instance_history 
ADD constraint pms_production_activity_instance_history_fk_05
FOREIGN KEY (id_product) 
REFERENCES pms_product (id);

ALTER TABLE pms_production_activity_instance_history 
ADD constraint pms_production_activity_instance_history_fk_06
FOREIGN KEY (id_order) 
REFERENCES pms_order (id);

/*SOLO UNA OCURRENCIA DE LA ACTIVIDAD DE PRODUCCION, PARA EL MISMO ITEM DE PEDIDO Y LA INSTANCIA 
DE PRODUCTO */
ALTER TABLE pms_production_activity_instance_history ADD CONSTRAINT
pms_production_activity_instance_history_uk_01 UNIQUE 
(id_production_activity,id_order_item,product_instance_unique_number);

ALTER TABLE pms_production_activity_instance_history ADD CONSTRAINT
pms_production_activity_instance_history_uk_02 UNIQUE (activity_instance_unique_number);




create table pms_production_activity_instance_raw_material_supply_history(
id	   		 		   				bigint,
id_production_activity_instance			bigint not null,
id_production_activity					bigint not null,
activity_description					varchar(250),
process_description						varchar(250),
id_raw_material_requirement				bigint not null,
id_raw_material						bigint not null,
raw_material_description					varchar(250),
id_measurment_unit						bigint not null,
measurment_unit_description				varchar(250),
quantity   							numeric(11,2) NOT NULL,
id_raw_material_existence_affected			bigint not null,
registration_date						timestamp not null default now(),
raw_material_effective_departure_date		timestamp,
creation_user							varchar(50),
creation_date							timestamp not null default now(),
last_modif_user						varchar(50),
last_modif_date						timestamp
);

ALTER TABLE pms_production_activity_instance_raw_material_supply_history ADD CONSTRAINT 
chk_raw_material_effective_departure_date CHECK (raw_material_effective_departure_date > registration_date);

ALTER TABLE pms_production_activity_instance_raw_material_supply_history ADD CHECK (quantity > 0);

ALTER TABLE pms_production_activity_instance_raw_material_supply_history 
ADD CONSTRAINT 
pms_production_activity_instance_raw_material_supply_history_id_pk PRIMARY KEY (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply_history ADD CONSTRAINT
pms_production_activity_instance_raw_material_supply_history_uk_01 UNIQUE 
(id_production_activity_instance,id_raw_material_requirement);

ALTER TABLE pms_production_activity_instance_raw_material_supply_history 
ADD constraint pms_production_activity_instance_raw_material_supply_history_fk_01
FOREIGN KEY (id_production_activity_instance) 
REFERENCES pms_production_activity_instance_history (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply_history 
ADD constraint pms_production_activity_instance_raw_mat_supply_history_fk_02
FOREIGN KEY (id_raw_material_requirement) REFERENCES pms_raw_material_requirement (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply_history 
ADD constraint pms_production_activity_instance_raw_mat_supply_history_fk_03
FOREIGN KEY (id_raw_material) REFERENCES pms_raw_material (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply_history 
ADD constraint pms_production_activity_instance_raw_mat_supply_history_fk_04
FOREIGN KEY (id_measurment_unit) REFERENCES pms_measurment_unit (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply_history 
ADD constraint pms_production_activity_instance_raw_mat_supply_history_fk_05
FOREIGN KEY (id_raw_material_existence_affected) REFERENCES pms_raw_material_existence (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply_history 
ADD constraint pms_production_activity_instance_raw_mat_supply_history_fk_06
FOREIGN KEY (id_production_activity) REFERENCES pms_production_process_activity (id);
