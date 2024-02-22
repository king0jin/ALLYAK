package com.example.allyak

import com.google.gson.annotations.SerializedName

data class PillInfo(
    @SerializedName("body")
    val body: Body?,
    @SerializedName("header")
    val header: Header?
)
data class Header(
    @SerializedName("resultCode")
    val resultCode: String?,
    @SerializedName("resultMsg")
    val resultMsg: String?
)
data class Body(
    @SerializedName("items")
    val items: List<Item?>?,
    @SerializedName("numOfRows")
    val numOfRows: Int?,
    @SerializedName("pageNo")
    val pageNo: Int?,
    @SerializedName("totalCount")
    val totalCount: Int?
)
data class Item(
    @SerializedName("BIZRNO")
    val bIZRNO: String?,
    @SerializedName("CHANGE_DATE")
    val cHANGEDATE: String?,
    @SerializedName("CHART")
    val cHART: String?,
    @SerializedName("CLASS_NAME") //병명?
    val cLASSNAME: String?,
    @SerializedName("CLASS_NO")
    val cLASSNO: String?,
    @SerializedName("COLOR_CLASS1")
    val cOLORCLASS1: String?,
    @SerializedName("COLOR_CLASS2")
    val cOLORCLASS2: Any?,
    @SerializedName("DRUG_SHAPE")
    val dRUGSHAPE: String?,
    @SerializedName("EDI_CODE")
    val eDICODE: Any?,
    @SerializedName("ENTP_NAME")
    val eNTPNAME: String?,
    @SerializedName("ENTP_SEQ")
    val eNTPSEQ: String?,
    @SerializedName("ETC_OTC_NAME")  //전문 or 일반
    val eTCOTCNAME: String?,
    @SerializedName("FORM_CODE_NAME")
    val fORMCODENAME: String?,
    @SerializedName("IMG_REGIST_TS")
    val iMGREGISTTS: String?,
    @SerializedName("ITEM_ENG_NAME")
    val iTEMENGNAME: Any?,
    @SerializedName("ITEM_IMAGE")
    val iTEMIMAGE: String?,
    @SerializedName("ITEM_NAME")
    val iTEMNAME: String?,
    @SerializedName("ITEM_PERMIT_DATE")
    val iTEMPERMITDATE: String?,
    @SerializedName("ITEM_SEQ")
    val iTEMSEQ: String?,
    @SerializedName("LENG_LONG")
    val lENGLONG: String?,
    @SerializedName("LENG_SHORT")
    val lENGSHORT: String?,
    @SerializedName("LINE_BACK")
    val lINEBACK: Any?,
    @SerializedName("LINE_FRONT")
    val lINEFRONT: Any?,
    @SerializedName("MARK_CODE_BACK")
    val mARKCODEBACK: Any?,
    @SerializedName("MARK_CODE_BACK_ANAL")
    val mARKCODEBACKANAL: String?,
    @SerializedName("MARK_CODE_BACK_IMG")
    val mARKCODEBACKIMG: String?,
    @SerializedName("MARK_CODE_FRONT")
    val mARKCODEFRONT: Any?,
    @SerializedName("MARK_CODE_FRONT_ANAL")
    val mARKCODEFRONTANAL: String?,
    @SerializedName("MARK_CODE_FRONT_IMG")
    val mARKCODEFRONTIMG: String?,
    @SerializedName("PRINT_BACK")
    val pRINTBACK: String?,
    @SerializedName("PRINT_FRONT")
    val pRINTFRONT: String?,
    @SerializedName("THICK")
    val tHICK: String?
)