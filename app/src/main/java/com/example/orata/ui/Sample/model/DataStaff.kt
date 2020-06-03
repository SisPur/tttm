package  com.example.orata.ui.Sample.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DataStaff : Serializable{

    @field:SerializedName("id")
    val staffId: String? = null

    @field:SerializedName("staff_name")
    val staffName: String? = null

    @field:SerializedName("staff_hp")
    val staffHp: String? = null

    @field:SerializedName("staff_alamat")
    val staffAlamat: String? = null
}