package uz.pdp.olxuz.models

class SmsClassRead {
     var _id: String? = null
     var _address: String? = null
     var _msg: String? = null
     var _readState:String? = null
     var _time: String? = null
     var _folderName: String? = null

    constructor(
        _id: String?,
        _address: String?,
        _msg: String?,
        _readState: String?,
        _time: String?,
        _folderName: String?
    ) {
        this._id = _id
        this._address = _address
        this._msg = _msg
        this._readState = _readState
        this._time = _time
        this._folderName = _folderName
    }
}