# SPRING COM SUPORTE A CACHE (REDIS) 
Essa aplicação consiste em utilizar o serviço de cache do Redis para armazenar em memória os produtos consultados.

## PRÉ-REQUISITOS
Aplicações que devem estar instaladas em sua máquina:
- [JDK](https://www.oracle.com/br/java/technologies/downloads/)
- [Docker](https://www.docker.com/products/docker-desktop/)
- IDE com suporte a Java de sua preferência

## SPRING
Para usar o serviço de cache no Spring você deve utilizar:

**Anotações**
- **@EnableCaching**: habilita o serviço de cache na aplicação.
- **@Cacheable**: executa o método caso o registro não esteja no cache e o adiciona no cache, caso contrário retorna o valor que está no cache sem a necessidade de executarPossui alta disponibilidade o método.
- **@CachePut**: executa o método e atualiza o cache.
- **@CacheEvict**: executa o método e remove o registro do cache.

**Dependências**
- **spring-boot-starter-cache**: habilita o suporte ao cache na sua aplicação Spring.
- **spring-boot-starter-data-redis**: utilizada para integrar o Redis à sua aplicação Spring.

**Configuração do projeto**
No arquivo application.properties devemos parametrizar o serviço de cache da aplicação.

Especifica onde o cache será armazenado:
```
spring.cache.type=redis
```

Especifica os dados de conexão com o REDIS:
```
spring.data.redis.host=127.0.0.1
spring.data.redis.port=6379
spring.data.redis.password=root
spring.data.redis.timeout=2000
```

## DOCKER
Para utilizar o serviço de cache iremos instanciar um container Redis no Docker, para isso execute os seguintes comandos no terminal:

Baixar a imagem mais recente do REDIS
```
docker pull redis
```

Criar o container com o serviço do REDIS
```
docker run --name redis-container -p 6379:6379 -d redis redis-server --requirepass root
```

Este comando cria o container com as seguintes características:
- **--name redis-container**: nomeia o container como redis-container.
- **-p**: especifica a porta que será exposta para a conexão.
- **-d**: roda o container em segundo plano (detached mode).
- **redis**: especifica a imagem do Redis que será usada, neste caso será utilizada a mais recente.
- **redis-server --requirepass**: define a senha de acesso ao Redis. **(Parâmetro opcional)**

**Conclusão**: ao executar o comando acima, foi criado um container chamado *redis-container* com o serviço do *Redis* rodando na porta *6379* que para acessá-lo será utilizada a senha *root*.

Caso queira remover posteriormente o container execute os seguintes comandos:
```
docker stop redis-container
docker rm redis-container
```

## REDIS
Redis (*Remote Dictionary Server*) é um sistema de armazenamento de dados em memória, utilizado principalmente para melhorar o desempenho de sistemas ao armazenar e recuperar dados de forma rápida, ao invés de consultar um banco de dados tradicional a cada solicitação. Dentre as características do Redis destacam-se:
- NoSQL (estrutura de chave e valor)
- Armazena os dados em memória
- Permite escalar horizontalmente
- Oferece respostas de baixa latência
- Possui alta disponibilidade
- Compatível com vários tipos de dados, como conjuntos classificados, hashes, conjuntos, listas e strings

Para acessar o Redis é necessário abrir o redis-cli, como ele encontra-se dentro do container, utilizamos o seguinte comando:

```
docker exec -it redis-container redis-cli -a root
```
*Obs: caso o container foi instanciado sem senha, remova o parâmetro **-a root***

### REDIS-CLI
O redis-cli é uma ferramenta de linha de comando (CLI) que permite interagir com o servidor Redis. 

Abaixo encontram-se alguns comandos úteis para manipulação dos dados no Redis:

Para listar todos os registros de uma determinada chave:
```
keys produtos::*
```

Para consultar uma chave:
```
get produtos::1
```

Para remover uma chave:
```
del produtos::1
```

Para limpar o cache:
```
flushdb
```