package com.inepex.nyomio.common.dto

data class DeviceInfo (val device: Device, val user: User, val company: Company? = null) {
}
