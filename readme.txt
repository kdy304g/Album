프로젝트 구조: Clean Architecture + MVVM

모듈간 의존성:         UI        (하위, data + player + Android Framework)
                 /     \
               Core   Player
                 \      /
                  Domain      (상위, 순수 코틀린)