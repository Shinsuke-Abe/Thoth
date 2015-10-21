
```
thoth 1.0
Usage: Thoth [options]

  -i <directory> | --inputBase <directory>
        inputBase is a required input directory property
  -o <dirctory> | --outputBase <dirctory>
        outputBase is a required output directory property
  --officeOutput
        officeOutput is convert markdown to docx file flag
```

実行にPandocとPlantUmlが必要ですが、これらをセットした実行イメージをDockerで作成しています。

イメージの実行は以下のコマンドで。

```
docker run --rm -v [ホストの入力ディレクトリ]:/in -v [ホストの出力ディレクトリ]:/out shinsukeabe/thoth [-i,-o以外のoption]
```