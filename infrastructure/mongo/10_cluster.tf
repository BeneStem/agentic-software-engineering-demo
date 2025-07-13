resource "mongodbatlas_cluster" "mongo_db_cluster" {
  project_id = var.mongo_atlas_project_id
  name = var.application_name
  num_shards = 1

  replication_factor = 3
  provider_backup_enabled = true
  auto_scaling_disk_gb_enabled = true
  mongo_db_major_version = "4.2"

  //Provider Settings "block"
  provider_name = "GCP"
  disk_size_gb = local.mongo_db_config[var.environment].disk_size_gb
  provider_instance_size_name = local.mongo_db_config[var.environment].provider_instance_size_name
  provider_region_name = upper(var.atlas_region)

  labels {
    key= "app"
    value = "${var.application_name}-backend-mongodbatlas_cluster"
  }

  labels {
    key = "team"
    value = var.team_key
  }
}

resource "mongodbatlas_database_user" "mongo_db_user" {
  project_id = var.mongo_atlas_project_id
  username = var.mongo_atlas_db_user_name
  password = var.mongo_atlas_db_user_password
  auth_database_name = "admin"

  roles {
    role_name = "readWrite"
    database_name = var.application_name
  }

  labels {
    key= "app"
    value = "${var.application_name}-backend-mongodbatlas_user"
  }

  labels {
    key = "team"
    value = var.team_key
  }
}
