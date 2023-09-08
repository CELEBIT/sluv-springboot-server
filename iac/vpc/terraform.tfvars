aws_region   = "ap-northeast-2"
cidr_numeral = "10"
vpc_name = "sluv-2nd-dev"

# Availability Zone list
availability_zones = ["ap-northeast-2a", "ap-northeast-2c"]

# ubuntu ec2 for jenkins instance
jenkins_base_ami = "ami-00fdfe418c69b624a"
jenkins_instance_type = "t2.small"
key_name = "SEC-SLUV-EC2-KEY"

# ubuntu ec2 for was instance
was_base_ami = "ami-00fdfe418c69b624a"
was_instance_type = "t2.micro"