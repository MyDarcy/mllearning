namespace java com.darcy.file

service Hello{
        string helloString(1:string para)
        i32 helloInt(1:i32 para)
        bool helloBoolean(1:bool para)
        void helloVoid()
        string helloNull()
}

service TFileService {
        list<TFile> ls(1:string path)
        string cat(1:string path, 2:TMode mode)
        TStatus upload(1:string name, 2:binary data)
        binary download(1:string path)
}

struct TFile {
     1: optional string path,
     2: optional string name,
     3: optional string mime,
     4: optional i64 size,
     5: optional TMode mode
}

enum TMode {
        TEXT,
        BINARY
}

enum TStatus {
        SUCCESS,
        FAILED
}