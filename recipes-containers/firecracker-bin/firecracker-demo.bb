SUMMARY = "firecracker demo"
DESCRIPTION = "this recipe shows firecracker in action, basic demo from quickstart guide"
HOMEPAGE = "https://github.com/firecracker-microvm/firecracker/blob/main/docs/getting-started.md"
LICENSE = "Apache-2.0"
# nooelint: oelint.var.licenseremotefile:License-File
LIC_FILES_CHKSUM = "file://LICENSE;md5=650b869bd8ff2aed59c62bad2a22a821"

S = "${WORKDIR}"

inherit features_check

# to run firecracker you need meta-virtualization
REQUIRED_DISTRO_FEATURES = "virtualization"

SRC_URI += "\
    https://raw.githubusercontent.com/firecracker-microvm/firecracker/main/LICENSE;name=license; \
    file://firecracker-demo \
    file://firecracker-demo.json \
"
SRC_URI[license.sha256sum] = "7c34d28e784b202aa4998f477fd0aa9773146952d7f6fa5971369fcdda59cf48"

RDEPENDS:${PN} += "\
        firecracker-bin \
        jailer-bin \
        wget \
"

do_install() {
        install -d ${D}${bindir}
        install firecracker-demo.json ${D}${bindir}
        install -m 0755 firecracker-demo ${D}${bindir}
}