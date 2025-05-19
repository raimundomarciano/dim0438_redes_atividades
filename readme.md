javac Weather.java Geo.java Config.java

java Weather "Parnamirim" "RN" "BR" 

a chave (api_key) deve ser colocada no arquivo config.properties (está como parte do .gitignore)

## WHEATER.AI

Se trata de uma conexão para consumo de uma API pública via Socket para realizar a visualização de dados climáticos de determinada
cidade, ou estado, a API realiza isso por verificação de longitude e latitude, e por isso utilizamos de uma API auxiliar do mesmo site
para que com o nome da cied, ou seu ZIP Code, possamos encontrar qual a sua latitude ou longiutde e assim verificar quais os dados climáticos
da região.

Para fazer as requisições ao serviço web foi realizado um SSLSocket pois esse serviço aceita somente esse tipo de contato via sockets, além dos
benefícios de autenticacção, segurança e 