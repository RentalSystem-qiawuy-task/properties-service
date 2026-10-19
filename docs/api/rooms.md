# RoomController — Phòng

Base path: `/api/v1/rooms`

| Method | Path | Query/body | Data payload |
|---|---|---|---|
| POST | `/api/v1/rooms` | `RoomRequest` | `RoomResponse` |
| GET | `/api/v1/rooms/{id}` | — | `RoomResponse` |
| GET | `/api/v1/rooms` | `buildingId`, `page`, `size`, `sort` | `Page<RoomResponse>` |
| GET | `/api/v1/rooms/building/{buildingId}` | `page`, `size` | `Page<RoomResponse>` |
| PUT | `/api/v1/rooms/{id}` | `RoomRequest` | `RoomResponse` |
| DELETE | `/api/v1/rooms/{id}` | — | `null` |

List mặc định `page=0`, `size=20`, `sort=createdAt,desc`; `page >= 0`, `1 <= size <= 100`. Sort hỗ trợ `createdAt`, `updatedAt`, `roomCode`, `status`, `basePrice` với hướng `asc`/`desc`. `buildingId` là filter tùy chọn.

## RoomRequest

```json
{"buildingId":"898785309339156480","roomTypeId":"898785309339156481","roomCode":"A-101","maxCapacity":4,"area":35.5,"bedroomCount":1,"bathroomCount":1,"basePrice":5000000,"status":"AVAILABLE"}
```

| Field | Kiểu | Bắt buộc | Ràng buộc |
|---|---|---:|---|
| `buildingId`, `roomTypeId` | string số dương | Có | ID liên kết |
| `roomCode` | string | Có | Không rỗng, tối đa 50 ký tự |
| `maxCapacity` | integer | Có | >= 1 |
| `area` | number | Có | >= 0.01 |
| `bedroomCount`, `bathroomCount` | integer | Có | >= 0 |
| `basePrice` | number | Có | >= 0 |
| `status` | string | Có | Ví dụ `AVAILABLE`; giá trị hợp lệ theo nghiệp vụ |

`RoomResponse`: `id`, `buildingId`, `roomTypeId` (đều string), `roomCode`, `maxCapacity`, `status`, `area`, `bedroomCount`, `bathroomCount`, `basePrice`, `createdAt`, `updatedAt`. Input/page/sort sai có thể trả `400`; tài nguyên không tồn tại có thể trả `404`.
