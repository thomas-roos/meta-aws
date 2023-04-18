FILESEXTRAPATHS:prepend:generic-arm64 := "${THISDIR}/files:"

SRC_URI:append:generic-arm64 = "\
    file://gravitonKernelConfigs.cfg \
    "