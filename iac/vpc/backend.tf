terraform {
  backend "s3" {
    bucket         = "sluv-2nd-id-apnortheast2-tfstate" # Set bucket name
    key            = "sluv/terraform/vpc/terraform.tfstate"
    region         = "ap-northeast-2"
    encrypt        = true
    dynamodb_table = "terraform-lock" # Set DynamoDB Table
  }
}