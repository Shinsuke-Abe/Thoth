# リポジトリ状況

## テスト

[![Circle CI](https://circleci.com/gh/Shinsuke-Abe/Thoth.svg?style=svg)](https://circleci.com/gh/Shinsuke-Abe/Thoth)

## カバレッジ

master [![Coverage Status](https://coveralls.io/repos/Shinsuke-Abe/Thoth/badge.svg?branch=master&service=github)](https://coveralls.io/github/Shinsuke-Abe/Thoth?branch=master)

develop [![Coverage Status](https://coveralls.io/repos/Shinsuke-Abe/Thoth/badge.svg?branch=develop&service=github)](https://coveralls.io/github/Shinsuke-Abe/Thoth?branch=develop)

# 使い方

## 実行

コマンドラインで実行したい場合は `build.sbt` の依存ライブラリと以下のオプションを参考にしてください。

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

依存ライブラリにパスを通したり実行にPandocとPlantUmlが必要です。

これらをセットしたイメージをDockerで作成しているのでそちらの実行を推奨します。イメージの実行は以下のコマンドで。

```
docker run --rm -v [ホストの入力ディレクトリ]:/in -v [ホストの出力ディレクトリ]:/out shinsukeabe/thoth [-i,-o以外のoption]
```

## 入力ディレクトリの構成ルール

入力ディレクトリは以下のルールで参照されます。

```
入力ディレクトリルート
  + /umls      ・・・ PlantUMLのファイル格納ディレクトリ(配下の .puml ファイルが対象)
  + /dots      ・・・ Graphvizのファイル格納ディレクトリ(配下の .dot ファイルが対象)
  + /resources ・・・ その他のリソースファイル群
  + hoge.md    ・・・ マークダウンファイル(複数)
  + /[subdir]  ・・・ 任意の名称のサブディレクトリ
```

サブディレクトリの構成も上記に従います。

`umls` と `dots` ディレクトリ以下は処理対象の拡張子が決まっており、ルールから外れたファイルはresourcesに出力されます。

## 出力ディレクトリの構成ルール

```
出力ディレクトリルート
  + markdown ・・・ マークダウンに展開したファイルの出力先。配下の構成は入力ディレクトリのルールと同じ。
  + office   ・・・ pandocで出力したofficeファイルの出力先。
```

PlantUMLとGraphvizのファイルは、pngに変換した上で出力されます。

## カスタムマークダウン

Thothが処理するカスタムマークダウンがあります。

### PlantUML用

PlantUMLは以下の書式でマークダウンに埋め込んでください。パスは相対パスで記述してください。

```
$[代替テキスト](.pumlファイルのパス "タイトル")
```

png形式で出力されたパスの画像書式に変換されます。

### Graphviz用

Graphvizは以下の書式でマークダウンに埋め込んでください。パスは相対パスで記述してください。

```
$[代替テキスト](.pumlファイルのパス "タイトル")
```

png形式で出力されたパスの画像書式に変換されます。