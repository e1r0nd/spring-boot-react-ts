/**
 * https://gist.github.com/meandmax/65b20f0dccf65f2854c3
 * get cookie by name without using a regular expression
 * */
export function getCookie(name: string) {
  function getCookieValues(cookie: string) {
    const cookieArray = cookie.split("=");
    return cookieArray[1].trim();
  }

  function getCookieNames(cookie: string) {
    const cookieArray = cookie.split("=");
    return cookieArray[0].trim();
  }

  const cookies = document.cookie.split(";");
  const cookieValue = cookies.map(getCookieValues)[
    cookies.map(getCookieNames).indexOf(name)
  ];

  return cookieValue === undefined ? null : cookieValue;
}
