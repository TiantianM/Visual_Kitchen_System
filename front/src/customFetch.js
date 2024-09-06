import { useRouter } from 'vue-router';

const customFetch = async (url, options) => {
  const router = useRouter();

  const token = localStorage.getItem('token');
  if (token) {
    if (!options.headers) {
      options.headers = {};
    }
    options.headers['Authorization'] = `Bearer ${token}`;
  }

  try {
    const baseURL = 'http://localhost:5050'
    const response = await fetch(baseURL + url, options);
    const result = response;
    if (!response.ok) {
      if (response.status === 403) {
        router.push({ path: '/' });
        return { response, ok: false };
      }
      throw new Error(`Request failed with status ${response.status}`);
    }
    return result;
  } catch (error) {
    console.error('Request failed:', error);
    throw error;
  }
};

export default customFetch;
