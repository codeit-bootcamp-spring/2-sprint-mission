### 비지니스 로직 vs 저장로직

|             | JCF*Service | File*Service |
|-------------|-------------|--------------|
|비지니스 로직   | create(),find(),findAll(),<br> update(), delete()|create(),find(),findAll(),<br> update(), delete()
|저장 로직      | HashMap <br> (data.put(), data.get(), data.rename())|File<br>(saveToFile(), loadFromFile() )