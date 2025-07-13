locals {
  mongo_db_config = {
    dev: {
      disk_size_gb: 10
      provider_instance_size_name: "M10"
    }
    prod: {
      disk_size_gb: 10
      provider_instance_size_name: "M20"
    }
  }
}
