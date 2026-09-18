# Vehicle OTA Cloud Platform

차량 OTA 관제 시나리오를 바탕으로 데이터 저장, CQRS, 컨테이너 배포와 Kubernetes 운영을 실습하는 프로젝트입니다.

## 현재 단계의 목적

현재 기능은 Spring Boot와 MySQL을 Docker Compose로 실행하는 것만으로도 동작합니다. CQRS와 Kubernetes는 현재 서비스 규모의 필수 조건이 아닙니다.

이 프로젝트에서는 도메인이 복잡해지기 전에 다음 항목을 각각 구현하고 동작 원리를 확인합니다.

현재는 OTA 도메인의 모든 기능을 만드는 것보다 Web Application, RDBMS, CQRS, NoSQL과 Kubernetes가 연결되는 과정을 직접 구성하고 검증하는 데 집중합니다.

이후 차량, 소프트웨어 패키지, 캠페인과 업데이트 상태 관리 기능을 추가해 OTA 배포 관제 플랫폼 시뮬레이션으로 확장합니다.

- Spring 애플리케이션과 MySQL의 데이터 저장·조회
- PV/PVC와 NFS를 이용한 데이터 유지
- Command와 Query 모델 분리
- NoSQL 조회 모델 생성
- RDBMS와 NoSQL 사이의 Projection 동기화
- Kubernetes 배포, 상태 확인과 장애 복구

초기 도메인을 단순하게 유지하면 원본 데이터 저장, 조회 모델 동기화와 배포 과정에서 발생하는 문제를 구분해서 확인할 수 있습니다. 여기서 검증한 구조는 OTA 도메인 기능을 확장할 때 다시 사용합니다.

Kubernetes는 애플리케이션 기능을 구현하기 위해 사용하는 것이 아니라 컨테이너 배포와 운영 과정을 확인하기 위해 사용합니다. Deployment와 Service, ConfigMap과 Secret, readiness/liveness probe, Ingress, PV/PVC, Pod 재생성과 데이터 유지를 직접 검증합니다.

## 구현 흐름

### 현재 구현

```text
Client
  → NGINX Ingress
  → ota-control-service
      ├─ Command API → MySQL → Projection Synchronizer → MongoDB
      └─ Query API ─────────────────────────────────────→ MongoDB
```

MySQL은 원본 데이터를 저장하고, MongoDB는 조회에 필요한 형태로 가공한 Projection을 저장합니다. 현재는 애플리케이션 내부에서 Projection을 동기화하며, 비동기 처리와 이벤트 재처리가 필요해지면 Kafka를 도입합니다.

현재 동기화는 한 프로세스 안에서 순차적으로 실행하는 이중 쓰기 방식입니다. MySQL과 MongoDB가 하나의 트랜잭션으로 묶이지 않으므로 중간 실패 시 데이터가 일치하지 않을 수 있습니다. 이후 운영 검증에서 Projection 재생성과 정합성 복구 방식을 추가하고, 비동기 처리 필요성이 확인되면 이벤트 발행 구조를 검토합니다.

## 진행 상태

| 범위 | 내용 | 상태 |
| --- | --- | --- |
| Web Application | OTA 작업 생성·조회 API와 MySQL 연동 | 완료 |
| Container | 멀티스테이지 Docker 이미지와 Docker Compose 구성 | 완료 |
| Kubernetes | Namespace, ConfigMap, Secret, Deployment, Service, Ingress | 완료 |
| Storage | PV/PVC와 NFS 기반 MySQL 데이터 저장 | 완료 |
| CQRS·NoSQL | Command와 Query 분리, MongoDB 조회 Projection 구성 | 완료 |
| 운영 검증 | 재시작, 데이터 동기화 실패, 장애 복구와 metric 확인 | 예정 |
| OTA 도메인 확장 | 차량, 패키지, 캠페인, 상태 머신과 시뮬레이터 | 이후 진행 |
| Kafka | 비동기 처리와 이벤트 재처리가 필요해질 때 도입 | 이후 검토 |

## 현재 구현한 API

```http
POST /api/v1/ota-updates
GET  /api/v1/ota-updates
```

업데이트 작업을 생성할 때 차량 ID와 목표 버전을 전달합니다.

```json
{
  "vehicleId": "VEHICLE-001",
  "targetVersion": "v1.0.1"
}
```

새 작업은 `PENDING` 상태와 진행률 `0`으로 저장됩니다. 현재는 작업 생성과 전체 조회까지만 구현되어 있으며, 상태 변경과 차량의 진행률 보고는 이후 범위입니다.

## OTA 배포 관제 서비스로의 확장

기술 실습을 완료한 뒤 다음 기능을 순서대로 추가합니다.

- Vehicle, SoftwarePackage, Campaign 관리
- 차량별 UpdateJob 생성
- 다운로드, 검증, 설치와 결과 보고 상태 머신
- 실패 재시도와 롤백
- 가상 차량 시뮬레이터
- 차량 수와 이벤트 처리량 증가에 따른 병목 측정
- 성공률, 실패율과 처리 지연 모니터링

업데이트 가능 여부는 초기에는 관제 서비스 내부 규칙으로 처리합니다. 판단 데이터와 배포 주기를 별도로 관리할 필요가 생기면 독립 서비스로 분리할 수 있습니다.

## 기술 스택과 버전

| 구분 | 버전 |
| --- | --- |
| 애플리케이션 | 0.0.1-SNAPSHOT |
| Java / Eclipse Temurin | 21 |
| Spring Boot | 4.1.1 |
| Gradle Wrapper | 9.5.1 |
| MySQL | 8.4 |
| MongoDB | 8.0.4 |
| Kubernetes | 1.30.14 |
| ingress-nginx | 1.11.3 |
| OTA 애플리케이션 이미지 | `kkr010128/ota-control-service:stage1-v1` |


## 프로젝트 범위

이 프로젝트는 OTA 서버와 클라우드 운영 구조를 학습하기 위한 시뮬레이션입니다.

- 실제 차량이나 ECU와 연결하지 않습니다.
- 실제 펌웨어와 bootloader를 구현하지 않습니다.
- 양산 환경의 보안, 안전 인증과 운영 기준을 재현하지 않습니다.
- NFS와 단일 랩 클러스터 구성은 Kubernetes 저장소와 복구 과정을 확인하기 위한 실습 환경입니다.
- 특정 기업의 시스템이나 기술 구조를 재현하지 않습니다.

## 문서

구현 과정에서 다음 내용을 별도로 기록합니다.

- 요구사항과 범위
- 아키텍처와 ADR
- 단계별 실습 기록
- 테스트 결과
- 배포 및 장애 대응 기록
