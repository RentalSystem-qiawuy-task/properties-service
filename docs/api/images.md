# ImageStorageController — Tải ảnh

Base path: `/api/v1/images`. Các endpoint dùng `multipart/form-data`.

| Method | Path | Multipart field | Data payload |
|---|---|---|---|
| POST | `/api/v1/images/buildings/{buildingId}` | `file` (một ảnh) | `StorageObject` |
| POST | `/api/v1/images/rooms/{roomId}` | `file` (một ảnh) | `StorageObject` |
| POST | `/api/v1/images/{folderName}` | `files` (một/nhiều ảnh) | `string[]` URL |

Ví dụ:

```bash
curl -X POST "http://localhost:8083/api/v1/images/buildings/898785309339156480" -F "file=@./building.png"
```

Chỉ hỗ trợ JPEG (`image/jpeg`), PNG (`image/png`) và WebP (`image/webp`); chữ ký file phải khớp MIME type. Kích thước mỗi ảnh tối đa mặc định 5 MiB (`STORAGE_IMAGE_MAX_SIZE`, byte); toàn request tối đa 50 MB. Owner ID phải dương. `folderName` chỉ gồm chữ/số ASCII, `_`, `-`, dài 1–80 ký tự.

Upload một ảnh trả `data` dạng:

```json
{"key":"building/898785309339156480/uuid.png","url":"https://storage.example/image.png","contentType":"image/png","size":123456}
```

Upload nhiều ảnh trả mảng URL string. File rỗng, MIME type không hỗ trợ, nội dung không khớp hoặc vượt giới hạn có thể trả `400`.
