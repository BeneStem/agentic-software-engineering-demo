export interface TagOptions {
  appName: string;
  filePath?: string;
}

export function createCssLinkTag(options: TagOptions): string {
  const { appName, filePath } = options;
  return filePath ? `<link rel='stylesheet' href='/finden/static/${appName}/client${filePath}' />` : '';
}

export function createScriptTag(options: TagOptions): string {
  const { appName, filePath } = options;
  return filePath ? `<script type='text/javascript' src='/finden/static/${appName}/client${filePath}'></script>` : '';
}
