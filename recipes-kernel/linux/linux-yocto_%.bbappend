FILESEXTRAPATHS:prepend:aws-ec2-graviton := "${THISDIR}/files:"

COMPATIBLE_MACHINE:aws-ec2-graviton = "aws-ec2-graviton"

SRC_URI:append:aws-ec2-graviton = "\
    file://gravitonKernelConfigs.cfg \
    "
