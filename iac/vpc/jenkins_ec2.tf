resource "aws_instance" "jenkins_ec2" {
  ami                    = var.jenkins_base_ami
  instance_type          = var.jenkins_instance_type
  key_name               = var.key_name
  subnet_id              = aws_subnet.public[0].id

  vpc_security_group_ids = ["${aws_security_group.jenkins_ec2_sg.id}"]

  tags = {
    Name = "${var.vpc_name}-jenkins-ec2"
  }

  depends_on = [aws_internet_gateway.default]
}

resource "aws_security_group" "jenkins_ec2_sg" {
  name        = "jenkins_ec2_sg"
  description = "allow 22, 80, 443, 8080"
  vpc_id      = aws_vpc.default.id
}

resource "aws_security_group_rule" "jenkins_sg_ssh" {
  type              = "ingress"
  from_port         = 22
  to_port           = 22
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.jenkins_ec2_sg.id
  description       = "ssh"
}

resource "aws_security_group_rule" "jenkins_sg_http" {
  type              = "ingress"
  from_port         = 80
  to_port           = 80
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.jenkins_ec2_sg.id
  description       = "http"
}

resource "aws_security_group_rule" "jenkins_sg_https" {
  type              = "ingress"
  from_port         = 443
  to_port           = 443
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.jenkins_ec2_sg.id
  description       = "https"
}

resource "aws_security_group_rule" "jenkins_sg_jenkins" {
  type              = "ingress"
  from_port         = 8080
  to_port           = 8080
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.jenkins_ec2_sg.id
  description       = "jenkins"
}

resource "aws_security_group_rule" "jenkins_sg_outbound" {
  type              = "egress"
  from_port         = 0
  to_port           = 0
  protocol          = "-1"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.jenkins_ec2_sg.id
  description       = "outbound"
}